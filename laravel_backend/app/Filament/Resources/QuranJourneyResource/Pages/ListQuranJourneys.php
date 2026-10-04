<?php

namespace App\Filament\Resources\QuranJourneyResource\Pages;

use App\Filament\Resources\QuranJourneyResource;
use Filament\Actions;
use Filament\Resources\Pages\ListRecords;

class ListQuranJourneys extends ListRecords
{
    protected static string $resource = QuranJourneyResource::class;

    protected function getHeaderActions(): array
    {
        return [
            Actions\CreateAction::make()->label('নতুন পর্যায় তৈরি করুন'),
        ];
    }
}
