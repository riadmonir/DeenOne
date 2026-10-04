<?php

namespace App\Filament\Resources;

use App\Filament\Resources\HadithResource\Pages;
use App\Models\Hadith;
use App\Models\HadithCategory;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class HadithResource extends Resource
{
    protected static ?string $model = Hadith::class;

    protected static ?string $navigationIcon = 'heroicon-o-book-open';
    protected static ?string $navigationLabel = 'হাদীস ভাণ্ডার';
    protected static ?string $modelLabel = 'হাদীস';
    protected static ?string $pluralModelLabel = 'হাদীসসমূহ';
    protected static ?string $navigationGroup = 'ইসলামিক কনটেন্ট ম্যানেজমেন্ট';
    protected static ?int $navigationSort = 2;

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\Section::make('হাদীসের তথ্য ও উৎস')
                    ->schema([
                        Forms\Components\Select::make('category_id')
                            ->label('ক্যাটাগরি')
                            ->relationship('category', 'name_bn')
                            ->searchable()
                            ->preload(),

                        Forms\Components\TextInput::make('book_name_bn')
                            ->label('কিতাবের নাম (বাংলা)')
                            ->required()
                            ->placeholder('সহীহ বুখারী, সহীহ মুসলিম ইত্যাদি'),

                        Forms\Components\TextInput::make('hadith_number')
                            ->label('হাদীস নম্বর')
                            ->placeholder('যেমন: ১, ১৮২৬'),

                        Forms\Components\TextInput::make('grade')
                            ->label('মান / গ্রেড')
                            ->default('সহীহ (Sahih)'),

                        Forms\Components\TextInput::make('narrator_bn')
                            ->label('বর্ণনাকারী (বাংলা)')
                            ->placeholder('হযরত আবু হুরায়রা (রা.)'),
                    ])->columns(2),

                Forms\Components\Section::make('আরবি ইবারত ও অর্থ')
                    ->schema([
                        Forms\Components\Textarea::make('arabic_text')
                            ->label('মূল আরবি পাঠ (Arabic Text)')
                            ->required()
                            ->rows(4)
                            ->extraAttributes(['dir' => 'rtl', 'style' => 'font-size: 1.2rem;']),

                        Forms\Components\Textarea::make('bangla_meaning')
                            ->label('বাংলা অনুবাদ ও শিক্ষা')
                            ->required()
                            ->rows(4),

                        Forms\Components\Toggle::make('is_active')
                            ->label('অ্যাপে সক্রিয় প্রদর্শন')
                            ->default(true),
                    ]),
            ]);
    }

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                Tables\Columns\TextColumn::make('book_name_bn')
                    ->label('কিতাব')
                    ->searchable()
                    ->weight('bold'),

                Tables\Columns\TextColumn::make('hadith_number')
                    ->label('নম্বর')
                    ->searchable(),

                Tables\Columns\TextColumn::make('narrator_bn')
                    ->label('বর্ণনাকারী')
                    ->limit(25),

                Tables\Columns\BadgeColumn::make('grade')
                    ->label('মান')
                    ->colors([
                        'success' => 'সহীহ (Sahih)',
                        'info' => 'হাসান (Hasan)',
                    ]),

                Tables\Columns\IconColumn::make('is_active')
                    ->label('সক্রিয়')
                    ->boolean(),
            ])
            ->filters([
                Tables\Filters\SelectFilter::make('category_id')
                    ->label('ক্যাটাগরি')
                    ->relationship('category', 'name_bn'),
            ])
            ->actions([
                Tables\Actions\EditAction::make(),
                Tables\Actions\DeleteAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListHadiths::route('/'),
            'create' => Pages\CreateHadith::route('/create'),
            'edit' => Pages\EditHadith::route('/{record}/edit'),
        ];
    }
}
