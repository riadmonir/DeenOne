<?php

namespace App\Filament\Resources;

use App\Filament\Resources\PushNotificationResource\Pages;
use App\Jobs\SendFcmBroadcastJob;
use App\Models\PushNotification;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Table;

class PushNotificationResource extends Resource
{
    protected static ?string $model = PushNotification::class;

    protected static ?string $navigationIcon = 'heroicon-o-bell-alert';
    protected static ?string $navigationLabel = 'পুশ নোটিফিকেশন';
    protected static ?string $modelLabel = 'নোটিফিকেশন';
    protected static ?string $pluralModelLabel = 'পুশ নোটিফিকেশনসমূহ';
    protected static ?string $navigationGroup = 'কমিউনিকেশন ও অ্যানাউন্সমেন্ট';
    protected static ?int $navigationSort = 1;

    public static function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\Section::make('নোটিফিকেশন বার্তা ও কনটেন্ট')
                    ->schema([
                        Forms\Components\TextInput::make('title')
                            ->label('শিরোনাম (Title)')
                            ->required()
                            ->maxLength(150),

                        Forms\Components\Textarea::make('body')
                            ->label('বিস্তারিত বার্তা (Body)')
                            ->required()
                            ->rows(3),

                        Forms\Components\Select::make('notification_type')
                            ->label('নোটিফিকেশন প্রকার (Type)')
                            ->options([
                                'ANNOUNCEMENT' => 'সাধারণ ঘোষণা (Announcement)',
                                'PRAYER_ALERT' => 'নামাজের ওয়াক্ত এলার্ট',
                                'DAILY_AYAH' => 'দৈনিক বরকতময় আয়াত',
                                'DAILY_HADITH' => 'দৈনিক নির্বাচিত হাদীস',
                                'APP_UPDATE' => 'নতুন অ্যাপ আপডেট',
                            ])
                            ->default('ANNOUNCEMENT')
                            ->required(),

                        Forms\Components\Select::make('priority')
                            ->label('অগ্রাধিকার (Priority)')
                            ->options([
                                'CRITICAL' => 'জরুরি (Critical)',
                                'HIGH' => 'উচ্চ (High)',
                                'NORMAL' => 'স্বাভাবিক (Normal)',
                            ])
                            ->default('HIGH')
                            ->required(),

                        Forms\Components\Select::make('target_audience')
                            ->label('টার্গেট অডিয়েন্স (Audience)')
                            ->options([
                                'ALL' => 'সকল ব্যবহারকারী (All Users)',
                                'BLOOD_DONORS' => 'শুধুমাত্র রক্তদাতাগণ (Blood Donors)',
                            ])
                            ->default('ALL')
                            ->required(),

                        Forms\Components\TextInput::make('image_url')
                            ->label('ব্যানার ইমেজ URL (ঐচ্ছিক)')
                            ->url()
                            ->placeholder('https://example.com/banner.jpg'),

                        Forms\Components\TextInput::make('target_action')
                            ->label('ডিপ-লিঙ্ক অ্যাকশন (Target Action)')
                            ->default('default')
                            ->placeholder('feature_quran, feature_hadith, feature_battle'),
                    ])->columns(2),
            ]);
    }

    public static function table(Table $table): Table
    {
        return $table
            ->columns([
                Tables\Columns\TextColumn::make('title')
                    ->label('শিরোনাম')
                    ->searchable()
                    ->weight('bold'),

                Tables\Columns\BadgeColumn::make('notification_type')
                    ->label('ধরন')
                    ->colors([
                        'primary' => 'ANNOUNCEMENT',
                        'warning' => 'PRAYER_ALERT',
                        'success' => 'DAILY_AYAH',
                        'info' => 'DAILY_HADITH',
                    ]),

                Tables\Columns\BadgeColumn::make('fcm_status')
                    ->label('স্ট্যাটাস')
                    ->colors([
                        'success' => 'SENT',
                        'danger' => 'FAILED',
                        'warning' => 'PENDING',
                    ]),

                Tables\Columns\TextColumn::make('sent_count')
                    ->label('প্রেরণ সংখ্যা')
                    ->sortable(),

                Tables\Columns\TextColumn::make('created_at')
                    ->label('তারিখ ও সময়')
                    ->dateTime('d M Y, h:i A')
                    ->sortable(),
            ])
            ->actions([
                Tables\Actions\Action::make('resend')
                    ->label('পুনরায় প্রেরণ')
                    ->icon('heroicon-o-paper-airplane')
                    ->color('success')
                    ->requiresConfirmation()
                    ->action(function (PushNotification $record) {
                        dispatch(new SendFcmBroadcastJob($record));
                    }),
                Tables\Actions\DeleteAction::make(),
            ]);
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListPushNotifications::route('/'),
            'create' => Pages\CreatePushNotification::route('/create'),
        ];
    }
}
