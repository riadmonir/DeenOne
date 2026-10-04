<?php

namespace App\Filament\Widgets;

use App\Models\BattleRoom;
use App\Models\BloodDonor;
use App\Models\PushNotification;
use App\Models\User;
use Filament\Widgets\StatsOverviewWidget as BaseWidget;
use Filament\Widgets\StatsOverviewWidget\Stat;

class StatsOverviewWidget extends BaseWidget
{
    protected static ?int $sort = 1;

    protected function getStats(): array
    {
        return [
            Stat::make('মোট অ্যাপ ব্যবহারকারী', User::count())
                ->description('নিবন্ধিত মোবাইল ইউজার')
                ->descriptionIcon('heroicon-m-user-group')
                ->color('success'),

            Stat::make('স্বেচ্ছাসেবী রক্তদাতা', BloodDonor::where('is_available', true)->count())
                ->description('সক্রিয় ও প্রস্তুত ডোনার')
                ->descriptionIcon('heroicon-m-heart')
                ->color('danger'),

            Stat::make('চলমান কুইজ ব্যাটল রুম', BattleRoom::where('status', 'WAITING')->orWhere('status', 'IN_PROGRESS')->count())
                ->description('লাইভ মাল্টিপ্লেয়ার ম্যাচ')
                ->descriptionIcon('heroicon-m-bolt')
                ->color('warning'),

            Stat::make('প্রেরিত পুশ নোটিফিকেশন', PushNotification::where('fcm_status', 'SENT')->count())
                ->description('সফল ব্রডকাস্ট ক্যাম্পেইন')
                ->descriptionIcon('heroicon-m-bell')
                ->color('info'),
        ];
    }
}
