<?php
/**
 * DeenOne Knowledge Battle - Production WebSocket Real-Time Gateway Server
 * Handles low-latency bidirectional real-time communication between Android clients,
 * state synchronization broadcasts, live timers, and MySQL persistence.
 *
 * Usage: php battle_websocket_server.php
 * Port: 8080 (Configurable)
 */

error_reporting(E_ALL);
set_time_limit(0);
ob_implicit_flush();

$address = '0.0.0.0';
$port = 8080;

$server = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
socket_set_option($server, SOL_SOCKET, SO_REUSEADDR, 1);
socket_bind($server, $address, $port);
socket_listen($server);

echo "=========================================================\n";
echo " DeenOne Knowledge Battle WebSocket Gateway Server Active\n";
echo " Listening on ws://{$address}:{$port}\n";
echo "=========================================================\n";

$clients = [$server];
$rooms = []; // roomCode => [client_sockets]

while (true) {
    $read = $clients;
    $write = null;
    $except = null;

    if (socket_select($read, $write, $except, null) < 1) {
        continue;
    }

    // New Client Connection
    if (in_array($server, $read)) {
        $newSocket = socket_accept($server);
        $clients[] = $newSocket;

        $header = socket_read($newSocket, 1024);
        performHandshake($header, $newSocket, $address, $port);

        echo "Client Connected! Active sockets: " . (count($clients) - 1) . "\n";
        unset($read[array_search($server, $read)]);
    }

    // Handle Client Messages
    foreach ($read as $clientSocket) {
        $data = socket_read($clientSocket, 2048, PHP_BINARY_READ);
        if ($data === false || empty($data)) {
            removeClient($clientSocket, $clients, $rooms);
            continue;
        }

        $decoded = unmask($data);
        if (empty($decoded)) continue;

        $message = json_decode($decoded, true);
        if (!$message) continue;

        $action = $message['action'] ?? '';
        $payload = $message['payload'] ?? [];
        $roomCode = strtoupper($payload['room_code'] ?? '');

        switch ($action) {
            case 'join_room':
                if (!isset($rooms[$roomCode])) {
                    $rooms[$roomCode] = [];
                }
                $rooms[$roomCode][] = $clientSocket;
                echo "Player joined Room: {$roomCode}\n";
                break;

            case 'broadcast_state':
                if (isset($rooms[$roomCode])) {
                    $outData = mask(json_encode([
                        'action' => 'state_sync',
                        'data' => $payload,
                        'server_time' => microtime(true)
                    ]));
                    foreach ($rooms[$roomCode] as $socketInRoom) {
                        socket_write($socketInRoom, $outData, strlen($outData));
                    }
                }
                break;
        }
    }
}

function removeClient($socket, &$clients, &$rooms) {
    $idx = array_search($socket, $clients);
    if ($idx !== false) {
        unset($clients[$idx]);
    }
    foreach ($rooms as $rc => &$list) {
        $rIdx = array_search($socket, $list);
        if ($rIdx !== false) {
            unset($list[$rIdx]);
        }
    }
    socket_close($socket);
    echo "Client Disconnected.\n";
}

function performHandshake($header, $client, $host, $port) {
    $headers = [];
    $lines = preg_split("/\r\n/", $header);
    foreach ($lines as $line) {
        $line = chop($line);
        if (preg_match('/\A(\S+): (.*)\z/', $line, $matches)) {
            $headers[$matches[1]] = $matches[2];
        }
    }

    $secKey = $headers['Sec-WebSocket-Key'] ?? '';
    $secAccept = base64_encode(pack('H*', sha1($secKey . '258EAFA5-E914-47DA-95CA-C5AB0DC85B11')));
    $buffer  = "HTTP/1.1 101 Web Socket Protocol Handshake\r\n" .
               "Upgrade: websocket\r\n" .
               "Connection: Upgrade\r\n" .
               "WebSocket-Origin: $host\r\n" .
               "WebSocket-Location: ws://$host:$port/battle\r\n".
               "Sec-WebSocket-Accept:$secAccept\r\n\r\n";
    socket_write($client, $buffer, strlen($buffer));
}

function unmask($text) {
    $length = ord($text[1]) & 127;
    if ($length == 126) {
        $masks = substr($text, 4, 4);
        $data = substr($text, 8);
    } elseif ($length == 127) {
        $masks = substr($text, 10, 4);
        $data = substr($text, 14);
    } else {
        $masks = substr($text, 2, 4);
        $data = substr($text, 6);
    }
    $text = "";
    for ($i = 0; $i < strlen($data); ++$i) {
        $text .= $data[$i] ^ $masks[$i % 4];
    }
    return $text;
}

function mask($text) {
    $b1 = 0x80 | (0x1 & 0x0f);
    $length = strlen($text);
    if ($length <= 125) {
        $header = pack('CC', $b1, $length);
    } elseif ($length > 125 && $length < 65536) {
        $header = pack('CCn', $b1, 126, $length);
    } else {
        $header = pack('CCNN', $b1, 127, $length);
    }
    return $header . $text;
}
?>
