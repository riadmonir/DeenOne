<?php

namespace App\Filament\Resources;

use App\Filament\Resources\BloodDonorResource\Pages;
use App\Models\BloodDonor;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class BloodDonorResource extends Resource
{
    protected static ?string $model = BloodDonor::class;

    protected static ?string $navigationIcon = 'heroicon-o-heart';
    protected static ?string $navigationLabel = 'রক্তদাতা ডিরেক্টরি';
    protected static ?string $modelLabel = 'রক্তদাতা';
    protected static ?string $pluralModelLabel = 'রক্তদাতাবৃন্দ';
    protected static ?string $navigationGroup = 'কমিউনিটি ও সমাজসেবা';
    protected static ?int $navigationSort = 3;

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\TextInput::make('name')
                    ->label('নাম')
                    ->required()
                    ->maxLength(100),

                Forms\Components\Select::make('blood_group')
                    ->label('রক্তের গ্রুপ')
                    ->options([
                        'A+' => 'A+',
                        'A-' => 'A-',
                        'B+' => 'B+',
                        'B-' => 'B-',
                        'AB+' => 'AB+',
                        'AB-' => 'AB-',
                        'O+' => 'O+',
                        'O-' => 'O-',
                    ])
                    ->required(),

                Forms\Components\TextInput::make('phone')
                    ->label('মোবাইল নম্বর')
                    ->tel()
                    ->required()
                    ->maxLength(30),

                Forms\Components\TextInput::make('district')
                    ->label('জেলা')
                    ->required(),

                Forms\Components\TextInput::make('area')
                    ->label('থানা / এলাকা')
                    ->placeholder('মিরপুর, গুলশান ইত্যাদি'),

                Forms\Components\DatePicker::make('last_donation_date')
                    ->label('সর্বশেষ রক্তদানের তারিখ'),

                Forms\Components\Toggle::make('is_available')
                    ->label('রক্তদানে প্রস্তুত (Available)')
                    ->default(true),

                Forms\Components\Toggle::make('is_verified')
                    ->label('অ্যাডমিন কর্তৃক ভেরিফাইড (Verified)')
                    ->default(false),
            ]);
    }

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                Tables\Columns\TextColumn::make('name')
                    ->label('নাম')
                    ->searchable()
                    ->weight('bold'),

                Tables\Columns\BadgeColumn::make('blood_group')
                    ->label('গ্রুপ')
                    ->colors([
                        'danger' => static fn ($state): bool => true,
                    ]),

                Tables\Columns\TextColumn::make('phone')
                    ->label('মোবাইল')
                    ->searchable(),

                Tables\Columns\TextColumn::make('district')
                    ->label('জেলা')
                    ->searchable(),

                Tables\Columns\IconColumn::make('is_available')
                    ->label('প্রস্তুত')
                    ->boolean(),

                Tables\Columns\IconColumn::make('is_verified')
                    ->label('ভেরিফাইড')
                    ->boolean(),
            ])
            ->filters([
                Tables\Filters\SelectFilter::make('blood_group')
                    ->label('রক্তের গ্রুপ')
                    ->options([
                        'A+' => 'A+', 'A-' => 'A-', 'B+' => 'B+', 'B-' => 'B-',
                        'AB+' => 'AB+', 'AB-' => 'AB-', 'O+' => 'O+', 'O-' => 'O-',
                    ]),
                Tables\Filters\TernaryFilter::make('is_verified')
                    ->label('ভেরিফিকেশন স্ট্যাটাস'),
            ])
            ->actions([
                Tables\Actions\EditAction::make(),
                Tables\Actions\DeleteAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListBloodDonors::route('/'),
            'create' => Pages\CreateBloodDonor::route('/create'),
            'edit' => Pages\EditBloodDonor::route('/{record}/edit'),
        ];
    }
}
