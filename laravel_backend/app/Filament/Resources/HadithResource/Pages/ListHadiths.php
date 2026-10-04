<?php

namespace App\Filament\Resources\HadithResource\Pages;

use App\Filament\Resources\HadithResource;
use Filament\Actions;
use Filament\Resources\Pages\ListRecords;

class ListHadiths extends ListRecords
{
    protected static string $resource = HadithResource::class;

    protected function getHeaderActions(): array
    {
        return [
            Actions\CreateAction::make()->label('নতুন হাদীস যুক্ত করুন'),
        ];
    }
}
