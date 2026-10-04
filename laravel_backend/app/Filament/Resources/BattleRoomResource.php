<?php

namespace App\Filament\Resources;

use App\Filament\Resources\BattleRoomResource\Pages;
use App\Models\BattleRoom;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class BattleRoomResource extends Resource
{
    protected static ?string $model = BattleRoom::class;

    protected static ?string $navigationIcon = 'heroicon-o-bolt';
    protected static ?string $navigationLabel = 'লাইভ ব্যাটল রুম';
    protected static ?string $modelLabel = 'ব্যাটেল রুম';
    protected static ?string $pluralModelLabel = 'কুইজ ব্যাটল রুমসমূহ';
    protected static ?string $navigationGroup = 'কমিউনিটি ও সমাজসেবা';
    protected static ?int $navigationSort = 5;

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                Tables\Columns\TextColumn::make('room_code')
                    ->label('রুম কোড')
                    ->weight('bold')
                    ->searchable(),

                Tables\Columns\TextColumn::make('host_name')
                    ->label('হোস্টের নাম')
                    ->searchable(),

                Tables\Columns\TextColumn::make('question_count')
                    ->label('প্রশ্ন সংখ্যা'),

                Tables\Columns\BadgeColumn::make('status')
                    ->label('স্ট্যাটাস')
                    ->colors([
                        'warning' => 'WAITING',
                        'primary' => 'IN_PROGRESS',
                        'success' => 'COMPLETED',
                        'danger' => 'CANCELLED',
                    ]),

                Tables\Columns\TextColumn::make('created_at')
                    ->label('তৈরির সময়')
                    ->dateTime('d M Y, h:i A')
                    ->sortable(),
            ])
            ->filters([
                Tables\Filters\SelectFilter::make('status')
                    ->options([
                        'WAITING' => 'অপেক্ষমাণ (Waiting)',
                        'IN_PROGRESS' => 'চলমান (In Progress)',
                        'COMPLETED' => 'সম্পন্ন (Completed)',
                        'CANCELLED' => 'বাতিল (Cancelled)',
                    ]),
            ])
            ->actions([
                Tables\Actions\Action::make('cancel')
                    ->label('রুম বন্ধ করুন')
                    ->icon('heroicon-o-x-circle')
                    ->color('danger')
                    ->requiresConfirmation()
                    ->action(fn (BattleRoom $record) => $record->update(['status' => 'CANCELLED'])),
                Tables\Actions\DeleteAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListBattleRooms::route('/'),
        ];
    }
}
