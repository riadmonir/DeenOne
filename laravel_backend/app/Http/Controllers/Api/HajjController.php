<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\HajjArticle;
use App\Models\HajjStage;
use App\Models\HajjTopic;
use Illuminate\Http\Request;

class HajjController extends Controller
{
    /**
     * Get hajj timeline stages
     */
    public function stages()
    {
        $stages = HajjStage::where('is_active', true)->orderBy('stage_number', 'asc')->get();
        return $this->success($stages);
    }

    /**
     * Get hajj topics
     */
    public function topics()
    {
        $topics = HajjTopic::where('is_active', true)->orderBy('display_order', 'asc')->get();
        return $this->success($topics);
    }

    /**
     * Get specific hajj article by key
     */
    public function article(string $key)
    {
        $article = HajjArticle::where('article_key', $key)->where('is_active', true)->first();
        if (!$article) {
            return $this->error('Article not found', 404);
        }
        return $this->success($article);
    }
}
