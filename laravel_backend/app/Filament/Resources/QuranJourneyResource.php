<?php

namespace App\Filament\Resources;

use App\Filament\Resources\QuranJourneyResource\Pages;
use App\Models\QuranStage;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class QuranJourneyResource extends Resource
{
    protected static ?string $model = QuranStage::class;

    protected static ?string $navigationIcon = 'heroicon-o-academic-cap';
    protected static ?string $navigationLabel = 'কুরআন জার্নি পর্যায়';
    protected static ?string $modelLabel = 'কুরআন জার্নি';
    protected static ?string $pluralModelLabel = 'কুরআন জার্নি পর্যায়সমূহ';
    protected static ?string $navigationGroup = 'ইসলামিক কনটেন্ট ম্যানেজমেন্ট';
    protected static ?int $navigationSort = 1;

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\TextInput::make('stage_number')
                    ->label('পর্যায় নম্বর')
                    ->numeric()
                    ->required(),

                Forms\Components\TextInput::make('title_bn')
                    ->label('শিরোনাম (বাংলা)')
                    ->required(),

                Forms\Components\TextInput::make('title_en')
                    ->label('শিরোনাম (ইংরেজি)')
                    ->required(),

                Forms\Components\TextInput::make('subtitle_bn')
                    ->label('উপশিরোনাম (বাংলা)'),

                Forms\Components\TextInput::make('badge_text_bn')
                    ->label('ব্যাজ টেক্সট (বাংলা)'),

                Forms\Components\Toggle::make('is_active')
                    ->label('সক্রিয়')
                    ->default(true),
            ]);
    }

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                Tables\Columns\TextColumn::make('stage_number')
                    ->label('পর্যায়')
                    ->sortable(),

                Tables\Columns\TextColumn::make('title_bn')
                    ->label('শিরোনাম')
                    ->weight('bold')
                    ->searchable(),

                Tables\Columns\BadgeColumn::make('badge_text_bn')
                    ->label('ব্যাজ'),

                Tables\Columns\TextColumn::make('lessons_count')
                    ->label('পাঠ সংখ্যা')
                    ->counts('lessons'),

                Tables\Columns\IconColumn::make('is_active')
                    ->label('সক্রিয়')
                    ->boolean(),
            ])
            ->actions([
                Tables\Actions\EditAction::make(),
                Tables\Actions\DeleteAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListQuranJourneys::route('/'),
            'create' => Pages\CreateQuranJourney::route('/create'),
            'edit' => Pages\EditQuranJourney::route('/{record}/edit'),
        ];
    }
}
