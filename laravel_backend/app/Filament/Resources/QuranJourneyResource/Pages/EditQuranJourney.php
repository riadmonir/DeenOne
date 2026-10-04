<?php

namespace App\Filament\Resources\QuranJourneyResource\Pages;

use App\Filament\Resources\QuranJourneyResource;
use Filament\Actions;
use Filament\Resources\Pages\EditRecord;

class EditQuranJourney extends EditRecord
{
    protected static string $resource = QuranJourneyResource::class;

    protected function getHeaderActions(): array
    {
        return [
            Actions\DeleteAction::make(),
        ];
    }
}
