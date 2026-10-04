<?php

namespace App\Filament\Resources\HadithResource\Pages;

use App\Filament\Resources\HadithResource;
use Filament\Actions;
use Filament\Resources\Pages\EditRecord;

class EditHadith extends EditRecord
{
    protected static string $resource = HadithResource::class;

    protected function getHeaderActions(): array
    {
        return [
            Actions\DeleteAction::make(),
        ];
    }
}
