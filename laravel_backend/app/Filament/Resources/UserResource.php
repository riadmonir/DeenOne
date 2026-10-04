<?php

namespace App\Filament\Resources;

use App\Filament\Resources\UserResource\Pages;
use App\Models\User;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class UserResource extends Resource
{
    protected static ?string $model = User::class;

    protected static ?string $navigationIcon = 'heroicon-o-users';
    protected static ?string $navigationLabel = 'মোবাইল ব্যবহারকারী';
    protected static ?string $modelLabel = 'ব্যবহারকারী';
    protected static ?string $pluralModelLabel = 'ব্যবহারকারীবৃন্দ';
    protected static ?string $navigationGroup = 'ইউজার ম্যানেজমেন্ট';
    protected static ?int $navigationSort = 4;

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\TextInput::make('name')
                    ->label('নাম')
                    ->required(),

                Forms\Components\TextInput::make('phone')
                    ->label('ফোন নম্বর'),

                Forms\Components\TextInput::make('district')
                    ->label('জেলা'),

                Forms\Components\TextInput::make('points')
                    ->label('মোট অর্জিত পয়েন্ট')
                    ->numeric(),

                Forms\Components\TextInput::make('streak_count')
                    ->label('ধারাবাহিক স্ট্রিক (Streak)')
                    ->numeric(),

                Forms\Components\Toggle::make('is_banned')
                    ->label('অ্যাকাউন্ট ব্লক / ব্যান')
                    ->helperText('ব্লক করা থাকলে ব্যবহারকারী কুইজ ব্যাটেলে যুক্ত হতে পারবে না।'),
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

                Tables\Columns\TextColumn::make('phone')
                    ->label('ফোন নম্বর')
                    ->searchable(),

                Tables\Columns\TextColumn::make('district')
                    ->label('জেলা')
                    ->searchable(),

                Tables\Columns\TextColumn::make('points')
                    ->label('পয়েন্ট')
                    ->sortable(),

                Tables\Columns\TextColumn::make('streak_count')
                    ->label('স্ট্রিক')
                    ->sortable(),

                Tables\Columns\IconColumn::make('is_banned')
                    ->label('ব্যানড')
                    ->boolean(),

                Tables\Columns\TextColumn::make('last_active')
                    ->label('সর্বশেষ সক্রিয়')
                    ->dateTime('d M Y, h:i A')
                    ->sortable(),
            ])
            ->filters([
                Tables\Filters\TernaryFilter::make('is_banned')
                    ->label('ব্যান স্ট্যাটাস'),
            ])
            ->actions([
                Tables\Actions\EditAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListUsers::route('/'),
            'edit' => Pages\EditUser::route('/{record}/edit'),
        ];
    }
}
