<?php

namespace App\Filament\Pages;

use App\Models\AppSetting;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Notifications\Notification;
use Filament\Pages\Page;

class AppSettingsPage extends Page implements Forms\Contracts\HasForms
{
    use Forms\Concerns\InteractsWithForms;

    protected static ?string $navigationIcon = 'heroicon-o-cog-6-tooth';
    protected static ?string $navigationLabel = 'অ্যাপস ও অ্যাড সেটিংস';
    protected static ?string $title = 'সিস্টেম ও অ্যাড কনফিগারেশন';
    protected static ?string $navigationGroup = 'সিস্টেম প্রশাসন';
    protected static ?int $navigationSort = 10;

    protected static string $view = 'filament.pages.app-settings';

    public ?array $data = [];

    public function mount(): void
    {
        $settings = AppSetting::all()->pluck('setting_value', 'setting_key')->toArray();
        $this->form->fill($settings);
    }

    public function form(Form $form): Form
    {
        return $form
            ->schema([
                Forms\Components\Tabs::make('SettingsTabs')
                    ->tabs([
                        Forms\Components\Tabs\Tab::make('অ্যাডমব কনফিগারেশন (AdMob)')
                            ->icon('heroicon-o-currency-dollar')
                            ->schema([
                                Forms\Components\Toggle::make('ads_enabled')
                                    ->label('অ্যাড প্রদর্শন সক্রিয় (Enable Ads)')
                                    ->default(true),

                                Forms\Components\TextInput::make('ad_frequency_interval')
                                    ->label('বিজ্ঞাপন প্রদর্শন বিরতি (ক্লিক সংখ্যা)')
                                    ->numeric()
                                    ->default(5),

                                Forms\Components\TextInput::make('admob_banner_id')
                                    ->label('AdMob ব্যানার ইউনিট ID')
                                    ->required(),

                                Forms\Components\TextInput::make('admob_interstitial_id')
                                    ->label('AdMob ইন্টারস্টিশিয়াল ইউনিট ID')
                                    ->required(),

                                Forms\Components\TextInput::make('admob_native_id')
                                    ->label('AdMob নেটিভ ইউনিট ID')
                                    ->required(),

                                Forms\Components\TextInput::make('admob_rewarded_id')
                                    ->label('AdMob রিওয়ার্ডেড ইউনিট ID')
                                    ->required(),
                            ])->columns(2),

                        Forms\Components\Tabs\Tab::make('রক্ষণাবেক্ষণ ও আপডেট (Maintenance)')
                            ->icon('heroicon-o-wrench-screwdriver')
                            ->schema([
                                Forms\Components\Toggle::make('maintenance_mode')
                                    ->label('মেইনটেন্যান্স মোড সক্রিয় (Maintenance Mode)'),

                                Forms\Components\Textarea::make('maintenance_message')
                                    ->label('রক্ষণাবেক্ষণ বার্তা (Maintenance Notice)')
                                    ->rows(2),

                                Forms\Components\TextInput::make('min_app_version')
                                    ->label('সর্বনিম্ন সমর্থিত সংস্করণ (Min Version Code)')
                                    ->numeric(),

                                Forms\Components\TextInput::make('latest_app_version')
                                    ->label('সর্বশেষ লাইভ সংস্করণ (Latest Version Code)')
                                    ->numeric(),

                                Forms\Components\Toggle::make('force_update')
                                    ->label('বাধ্যতামূলক আপডেট কার্যকর (Force Update)'),

                                Forms\Components\TextInput::make('update_url')
                                    ->label('প্লে স্টোর আপডেট লিংক (Play Store URL)'),
                            ])->columns(2),

                        Forms\Components\Tabs\Tab::make('ঘোষণা ও ব্যানার (Announcements)')
                            ->icon('heroicon-o-megaphone')
                            ->schema([
                                Forms\Components\Toggle::make('announcement_active')
                                    ->label('হোম স্ক্রিন ঘোষণা ব্যানার সক্রিয়'),

                                Forms\Components\TextInput::make('announcement_banner_text')
                                    ->label('ব্যানার টেক্সট (বাংলা)')
                                    ->columnSpanFull(),

                                Forms\Components\TextInput::make('announcement_deep_link')
                                    ->label('ডিপ-লিঙ্ক টার্গেট (Deep Link)'),

                                Forms\Components\Textarea::make('daily_ayah_text')
                                    ->label('আজকের নির্বাচিত আয়াত')
                                    ->rows(2),

                                Forms\Components\Textarea::make('daily_hadith_text')
                                    ->label('আজকের নির্বাচিত হাদীস')
                                    ->rows(2),
                            ])->columns(2),
                    ]),
            ])
            ->statePath('data');
    }

    public function submit(): void
    {
        $state = $this->form->getState();

        foreach ($state as $key => $value) {
            AppSetting::updateOrCreate(
                ['setting_key' => $key],
                ['setting_value' => (string)$value]
            );
        }

        Notification::make()
            ->title('সেটিংস সফলভাবে সংরক্ষিত হয়েছে!')
            ->success()
            ->send();
    }
}
