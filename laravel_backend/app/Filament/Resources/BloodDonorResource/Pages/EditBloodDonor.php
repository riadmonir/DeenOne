<?php

namespace App\Filament\Resources\BloodDonorResource\Pages;

use App\Filament\Resources\BloodDonorResource;
use Filament\Actions;
use Filament\Resources\Pages\EditRecord;

class EditBloodDonor extends EditRecord
{
    protected static string $resource = BloodDonorResource::class;

    protected function getHeaderActions(): array
    {
        return [
            Actions\DeleteAction::make(),
        ];
    }
}
