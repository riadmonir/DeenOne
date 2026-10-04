# Mandatory Project Operational Rules (স্থায়ী প্রকল্প পরিচালনা নিয়মাবলী)

## সর্বকালীন ও অপরিবর্তনীয় নির্দেশনা (Permanent Standing Instruction)
> **Note: প্রোজেক্ট যতদিন চলবে ততদিন আমি যেই কমান্ড ই দেই না কেন, এই প্রম্পট অনুযায়ী বাধ্যতামূলক কাজ করবে। আমি যে Screenshot দেব এবং যে Section mention করব, শুধু সেই Section-টিই production-ready করে দেবে। DeenOne-এর existing UI/UX, color system (Dark Mode / Light Mode), বাংলা/English language change mode অক্ষুণ্ণ রেখে Screenshot-এর design 100% accurately implement করবে। স্ক্রিনশটের প্রতিটি টেক্সট, হেডিং, আয়াত, দোয়া, রেফারেন্স ও উপাদান হুবহু (Verbatim) লাইন-বাই-লাইন রাখবে; কোনো কিছু বাদ দেওয়া, সংক্ষিপ্ত করা, নিজের মতো মনগড়া লেখা বা মিসিং রাখা সম্পূর্ণ নিষিদ্ধ। অহেতুক অ্যানিমেশন ছাড়া টাচ অ্যানিমেশন শুধুমাত্র এবং একান্তভাবেই নির্দিষ্ট বাটনের (Button) মধ্যে থাকবে; কোনো কার্ড ভিউতে (CardView / Content Card / লিস্ট আইটেম কার্ড) কোনো টাচ অ্যানিমেশন থাকা সম্পূর্ণ নিষিদ্ধ। প্রয়োজন ছাড়া Toast ব্যবহার করবে না এবং কোনো existing code edit/delete করবে না। মেমোরি ও স্ক্রোলিংয়ে স্মার্ট লেজি লোডিং (Lazy Loading) নিশ্চিত করবে যাতে অ্যাপস সবসময় লাইটওয়েট ও super-fast থাকে। প্রতিবার অ্যাপস বিল্ড বা কোড ইমপ্লিমেন্ট করার সময় অ্যাপসের সকল ডাটা MySQL ডাটাবেজের সাথে কানেক্টেড হয়েছে কিনা, অটো সিঙ্ক (Auto Sync) ডাটা ইমপোর্ট ও এক্সপোর্ট সঠিকভাবে কার্যকর হয় কিনা, পিএইচপি অ্যাডমিন প্যানেলের সাথে শতভাগ কানেক্টেড কিনা এবং ডাটাবেজ/কোয়েরিতে কোনো SQL error আছে কিনা তা বাধ্যতামূলকভাবে চেক করতে হবে। কোনো error, bug, crash, hang, lag বা slow performance থাকা যাবে না। কোনো unnecessary code file রাখা যাবে না। App অবশ্যই super-fast, ultra-smooth, all device stable ও fully optimized হতে হবে।**

---

## মূল স্তম্ভসমূহ (Core Execution Principles)

### ১. সর্বোচ্চ বাধ্যতামূলক অগ্রাধিকার: সুপার-ফাস্ট ও ল্যাগ-মুক্ত ৬০ FPS পারফরম্যান্স (Mandatory 1st Priority: Superfast, Highly Optimized, Clean & Smooth Performance, Fast Data Transmission, Minimal Loading Time, and Lag-free 60 FPS UI)
- **সুপার-ফাস্ট ও ল্যাগ-মুক্ত ৬০ FPS এক্সপেরিয়েন্স:** অ্যাপ্লিকেশনের প্রতিটি স্ক্রিন, ট্রানজিশন, স্ক্রোলিং ও ইন্টারঅ্যাকশন সম্পূর্ণ ল্যাগ-মুক্ত, মসৃণ এবং ফ্লুইড ৬০ FPS (Frames Per Second) রেন্ডারিং সম্পন্ন হতে হবে। কোনো ধরনের ফ্রেম ড্রপ, স্টাটারিং, স্ক্রিন ফ্রিজ বা হ্যাং থাকা সম্পূর্ণ নিষিদ্ধ।
- **ফাস্ট ডাটা ট্রান্সমিশন ও মিনিমাল লোডিং টাইম:** ব্যাকএন্ড API, ডাটাবেজ কোয়েরি এবং লোকাল ক্যাশ থেকে ডাটা ট্রান্সমিশন চোখের পলকে (Ultra-fast & instant) সম্পন্ন হতে হবে। অপ্রয়োজনীয় নেটওয়ার্ক রিকোয়েস্ট বর্জন করে লোডিং টাইম সর্বনিম্ন পর্যায়ে (Zero/Minimal Loading Time) নামিয়ে আনতে হবে।
- **ক্লিন ও হাইলি অপ্টিমাইজড মেমোরি ম্যানেজমেন্ট:** প্রতিটি কোড, ভিউ ও রিসোর্স মেমোরি-দক্ষ (Memory-efficient) হতে হবে। ব্যাকগ্রাউন্ড থ্রেডিং (Executors/Coroutines/Async), ভিউ রিসাইক্লিং ও রিসোর্স ক্লিয়ারেন্সের মাধ্যমে মেমোরি লিক ও প্রসেসর থ্রটলিং পুরোপুরি বন্ধ করতে হবে।
- **অল-ডিভাইস স্ট্যাবিলিটি ও লাইটওয়েট আর্কিটেকচার:** লো-এন্ড ডিভাইস থেকে শুরু করে ফ্ল্যাগশিপ সব ধরনের অ্যান্ড্রয়েড ডিভাইসে অ্যাপ সবসময় সুপার-ফাস্ট, লাইটওয়েট ও রিলায়েবল থাকবে।

### ২. কঠোর স্কোপ নিয়ন্ত্রণ (Strict Scope Control)
- ইউজার যে স্ক্রিনশট দেবেন এবং যে সেকশন উল্লেখ করবেন, শুধুমাত্র সেই নির্দিষ্ট সেকশনটিই সম্পূর্ণ প্রোডাকশন-রেডি করতে হবে।
- অন্য কোনো সেকশন স্পর্শ বা অনাকাঙ্ক্ষিত পরিবর্তন করা যাবে না।

### ৩. শতভাগ ভিজ্যুয়াল সামঞ্জস্য, কালার কনসিস্টেন্সি ও ইউনিফর্ম কার্ড আর্কিটেকচার (100% Visual Fidelity, Color Consistency & Uniform Card Architecture)
- **থিম টোকেনের সার্বজনীন ব্যবহার:** কোনো হার্ডকোডেড অস্বস্তিকর রং বা ইনকনসিস্টেন্ট কালার ব্যবহার করা সম্পূর্ণ নিষিদ্ধ। থিম টোকেন (`@color/bg_main`, `@color/bg_card`, `@color/text_primary`, `@color/text_secondary`, `@color/border_card`, `@color/accent_teal`, `@color/accent_gold`, `@color/accent_mint` ইত্যাদি) ব্যবহারের মাধ্যমে পুরো অ্যাপ জুড়ে শতভাগ কালার কনসিস্টেন্সি নিশ্চিত করতে হবে।
- **বাটন ও ফন্ট ভিজিবিলিটি ও স্পষ্ট কন্ট্রাস্ট:** কোনো বাটন বা টেক্সট যাতে ব্যাকগ্রাউন্ডের সাথে মিশে না যায় বা পড়তে অস্পষ্ট না হয়। ডার্ক মোড এবং লাইট মোড উভয়েই টেক্সট এবং বাটনের ব্যাকগ্রাউন্ডের চমৎকার কালার কন্ট্রাস্ট ও স্পষ্ট ভিজিবিলিটি থাকতে হবে।
- **আইকন ও শেপের সামঞ্জস্য:** আইকন অবশ্যই সংশ্লিষ্ট বিষয়ের সাথে প্রাসঙ্গিক (topic-related) হতে হবে এবং স্ক্রিনশটের শেপ, স্টাইল ও ভিজ্যুয়াল অ্যাপিয়ারেন্স হুবহু বজায় রাখতে হবে।
- **বিস্তারিত কার্ডের ইউনিফর্ম ডিজাইন পলিসি (Standard Uniform Expandable Card Policy):** অ্যাপের যে সকল সেকশনে বিস্তারিত পড়ার মতো তালিকা বা কার্ড থাকবে (যেমন: রোজার দোয়া, সিয়ামের তাৎপর্য ও ফযীলত, আরকান, রোযার বিভিন্ন আদব, রোযায় যা বৈধ, রোযায় অপছন্দনীয়, রমাদ্বানের কর্তব্য, বিদআত, যয়ীফ ও জাল হাদিস, ফিতরা, নিষিদ্ধ দিন ইত্যাদি)—সেগুলোর প্রতিটি কার্ড এবং হেডিংয়ের ডিজাইন হুবহু একই (Uniform & Identical) হতে হবে। এক পেজে এক ডিজাইন এবং আরেক পেজে আরেক ডিজাইন করা বা মনগড়া একেক পেজে আলাদা ব্যাকগ্রাউন্ড/কালার বানানো সম্পূর্ণ নিষিদ্ধ।
- **কার্ড হেডিংয়ের ক্লিন স্টাইল (Clean Card Heading without Background Pill):** কার্ডের ভেতরের শিরোনামে কোনো মনগড়া রঙিন ব্যাকগ্রাউন্ড পিল (যেমন `@drawable/bg_badge_pill_teal`) বা বেমানান কালার ব্যবহার করা সম্পূর্ণ নিষিদ্ধ। প্রতিটি কার্ডের শিরোনাম হবে স্ট্যান্ডার্ড `@color/text_primary`, ১৭sp বোল্ড, ক্লিন ও ব্যাকগ্রাউন্ড-হীন (হুবহু "রোজার দোয়া" ও `ItemHajjHistoryCardBinding`-এর মতো)।
- **ইউনিফর্ম কার্ড ইন্টারঅ্যাকশন:** ডিফল্টভাবে প্রতিটি কার্ড সংক্ষিপ্ত (২ লাইনের প্রিভিউ + "বিস্তারিত দেখুন ⌵") থাকবে এবং কার্ড বা বাটনে ক্লিক করলে তা পূর্ণাঙ্গ আকারে বিস্তারিত বিবরণী প্রদর্শন করবে। কোনো কার্ডভিউতে টাচ অ্যানিমেশন থাকবে না (Rule 7)।

### ৪. স্ক্রিনশটের কন্টেন্ট ও উপাদানের শতভাগ অবিকল বিশ্বস্ততা (100% Verbatim Content & Zero Omission)
- **কোনো মনগড়া বা প্যারাফ্রেজ নয়:** স্ক্রিনশটে থাকা প্রতিটি টেক্সট, হেডিং, সাব-টাইটেল, আয়াত, হাদিস, বাংলা অর্থ, রেফারেন্স, ফুটনোট বা নোট অক্ষর-অক্ষর (verbatim) হুবহু কোডে বসাতে হবে। কোনো অবস্থাতেই নিজের মতো সংক্ষিপ্ত করা, পরিবর্তন করা বা মনগড়া কোনো কিছু লেখা সম্পূর্ণ নিষিদ্ধ।
- **উপাদান ও তথ্যের পূর্ণাঙ্গতা (Zero Missing Elements):** স্ক্রিনশটের কোনো উপাদান—যেমন ছোট ব্যাজ, কাউন্টার, অ্যারো, আইকন, সাবটেক্সট, সিরিয়াল নম্বর বা কোনো অপশন—বাদ বা মিসিং রাখা যাবে না। স্ক্রিনশটে যা দৃশ্যমান, তার ১০০% কোডে বিদ্যমান থাকতে হবে।
- **অস্পষ্টতায় সরাসরি যাচাই:** স্ক্রিনশটের কোনো লেখা বা অংশ যদি অস্পষ্ট বা পড়তে অসুবিধা হয়, তবে অনুমান করে কিছু না লিখে সরাসরি ইউজারের কাছ থেকে সঠিক তথ্য জেনে নিয়ে তারপর কাজ করতে হবে।

### ৫. সর্বজনীন ডুয়েল-ল্যাঙ্গুয়েজ ও ব্র্যাকেট-মুক্ত ক্লিন পলিসি (Universal Dual-Language & Clean Language Mode)
- **আরবি ব্যতীত প্রতিটি শব্দের বাংলা ও ইংরেজি উপস্থিতি:** বিশুদ্ধ আরবি ক্যালিগ্রাফি এবং কুরআন-হাদিসের মূল আরবি ইবারত ব্যতীত অ্যাপের প্রতিটি শব্দ, শিরোনাম, সাব-টাইটেল, কার্ড, বাটন, ডায়লগ, সেটিংস, টগল ও ফিচারে বাংলা ও ইংরেজি অনুবাদ যুগপৎ বিদ্যমান থাকতে হবে।
- **ব্র্যাকেট পল্যুশন সম্পূর্ণ নিষিদ্ধ (No Mixed Language / Bracket Text):**
  - **বাংলা (`bn`) মোড:** বাংলা সিলেক্ট থাকলে পেজের নাম, বাটন বা টেক্সটে কোনো ইংরেজি ব্র্যাকেট রাখা যাবে না (যেমন: `সালাহ টাইম এডজাস্টমেন্ট (Salah Time Adjustment)` এমন মিশ্রণ নিষিদ্ধ; শুধু `সালাত সময় সমন্বয়` থাকবে)।
  - **ইংরেজি (`en`) মোড:** ইংরেজি সিলেক্ট থাকলে কোনো বাংলা ব্র্যাকেট বা মিশ্রণ রাখা যাবে না (যেমন: `Salah Time Adjustment (সালাহ সময়)` নিষিদ্ধ; শুধু `Salah Time Adjustment` থাকবে)।
- **ভাষানুযায়ী শতভাগ টোস্ট ও নোটিফিকেশন সিঙ্ক (Language-Synchronized Notifications & Toasts):**
  - বাংলা মোডে সমস্ত ইন-অ্যাপ নোটিফিকেশন, পুশ নোটিফিকেশন ও প্রয়োজনীয় টোস্ট খাঁটি বাংলায় প্রদর্শিত হবে।
  - ইংরেজি মোডে সমস্ত ইন-অ্যাপ নোটিফিকেশন, পুশ নোটিফিকেশন ও প্রয়োজনীয় টোস্ট খাঁটি ইংরেজিতে প্রদর্শিত হবে।

### ৬. স্মার্ট লেজি লোডিং ও সুপার-ফাস্ট পারফরম্যান্স (Smart Lazy Loading & Instant Performance)
- **অন-ডিমান্ড ও লেজি লোড আর্কিটেকচার:** কোনো ট্যাবে বা স্ক্রিনে ইউজার প্রবেশ না করা পর্যন্ত অপ্রয়োজনীয় ব্যাকগ্রাউন্ড রিসোর্স বা বড় ডেটাসেট প্রি-লোড করে অ্যাপকে ভারী করা যাবে না।
- **স্ক্রোল-বেসড লেজি লোডিং:** বড় লিস্ট বা জটিল পেজে ইউজার যতটুকু স্ক্রোল করবেন, পেজিং ও ভিউ রিসাইক্লিংয়ের মাধ্যমে ঠিক ততটুকুই লোড হবে।
- **ইনস্ট্যান্ট রেসপন্স:** লেজি লোডের কারণে যেন কোনো স্ক্রিন লোড হতে দেরি না হয় (১-২ সেকেন্ডের বেশি অপেক্ষা করানো যাবে না)। ভিউ রেন্ডারিং এবং ডাটাবেজ কুয়েরি অ্যাসিনক্রোনাস ব্যাকগ্রাউন্ড থ্রেডে চোখের পলকে সম্পন্ন হতে হবে।

### ৭. শুধুমাত্র বাটনে টাচ অ্যানিমেশন ও কার্ড ভিউতে অ্যানিমেশন সম্পূর্ণ বর্জন পলিসি (Touch Animation ONLY on Buttons & STRICT ZERO Touch Animation on Cards)
- **টাচ অ্যানিমেশন শুধুমাত্র বাটনে সীমাবদ্ধ (Touch Animation Exclusively for Buttons):** অ্যাপের টাচ অ্যানিমেশন (`TouchAnimationUtil.attachTouchSpring` বা অনুরূপ স্প্রিং/প্রেস ইফেক্ট) শুধুমাত্র এবং একান্তভাবেই নির্দিষ্ট অ্যাকশন বাটন, ব্যাক বাটন, আইকন বাটন বা ডায়লগ বাটনে প্রযুক্ত হবে।
- **কার্ড ভিউতে টাচ অ্যানিমেশন সম্পূর্ণ নিষিদ্ধ (Strict Zero Touch Animation on CardViews):** অ্যাপের কোনো কার্ড ভিউ—যেমন হাদিস কার্ড, আয়াত কার্ড, চ্যাপ্টার কার্ড, ক্যাটাগরি কার্ড, কনটেন্ট কার্ড বা তালিকা কার্ডে—কোনো অবস্থাতেই কোনো টাচ অ্যানিমেশন যুক্ত করা যাবে না। কার্ডে ক্লিক করলে কোনো সংকোচন, স্কেল বা স্প্রিং ইফেক্ট হবে না, সরাসরি নির্ধারিত অ্যাকশন বা পেজ ওপেন হবে।
- **নতুন ফিচারে ম্যান্ডেটরি রুলস বাস্তবায়ন (Mandatory Rule Implementation for Every New Feature):** ভবিষ্যতে অ্যাপে যেকোনো নতুন ফিচার, সেকশন, ডায়লগ বা স্ক্রিন যোগ করার সময় এই স্থায়ী পরিচালনা নির্দেশিকার (Mandatory Rules List) প্রতিটি নিয়ম শতভাগ বাধ্যতামূলকভাবে (Strictly & Mandatorily) অক্ষরে অক্ষরে বাস্তবায়ন করতে হবে।

### ৮. টোস্ট বর্জন নীতি (Zero Unnecessary Toasts)
- সুনির্দিষ্ট ও অতি প্রয়োজনীয় ক্ষেত্র (যেমন: ক্লিপবোর্ডে কপি সম্পন্ন) ছাড়া যত্রতত্র কোনো অপ্রয়োজনীয় Toast মেসেজ ব্যবহার করা যাবে না।

### ৯. বিদ্যমান কোডের অখণ্ডতা রক্ষা (Preserve Existing Code)
- কোনো বিদ্যমান প্রয়োজনীয় কোড মুছে ফেলা বা অহেতুক সম্পাদনা করা যাবে না।
- নতুন ফিচার বিদ্যমান আর্কিটেকচারের সাথে নির্বিঘ্নে যুক্ত করতে হবে।

### ১০. ত্রুটিহীনতা, নিখুঁত লজিক ও টেস্ট ভেরিফিকেশন (Zero Error, Robust Logic & Build Stability)
- অ্যাপের সমস্ত ইন্টারঅ্যাকশন ফাংশনালি ও লজিক্যালি ১০০% নির্ভুল হতে হবে।
- কোনো error, bug, crash, hang, lag বা slow performance থাকা চলবে না।
- প্রতিটি কাজের পর গ্র্যাডেল বিল্ড (`./gradlew assembleDebug`) যাচাই করে ০ এরর ও ০ ওয়ার্নিং নিশ্চিত করতে হবে।
- **ডাটাবেজ ও এসকিউএল এরর যাচাই:** প্রতিবার কোড তৈরি বা গ্র্যাডেল বিল্ডের সময়েই ব্যাকএন্ড এপিআই ও ডাটাবেজের সংযোগ, পিএইচপি ফাইলের সিনট্যাক্স (`php -l`), এবং `server_backend/deenone_db.sql`-এর সমস্ত কোয়েরিতে ০ এসকিউএল এরর (Zero SQL Error) ও অ্যাডমিন প্যানেল সিঙ্ক বাধ্যতামূলকভাবে যাচাই করতে হবে।
- কোনো অপ্রয়োজনীয় বা ডুপ্লিকেট কোড ফাইল রাখা যাবে না।

### ১১. যুগপৎ ফুল-স্ট্যাক ডেভেলপমেন্ট ও ডাটাবেজ সিঙ্ক (Simultaneous App + PHP Admin Panel + Database Sync)
- **একসাথে উভয় প্রান্ত প্রস্তুতকরণ:** যে সকল ফিচার বা ডেটা ব্যাকএন্ড/ডাটাবেজের সাথে সম্পৃক্ত, সেগুলোর জন্য আলাদা কমান্ডের অপেক্ষা না করে **একসাথেই অ্যান্ড্রয়েড জাভা কোড, পিএইচপি রেস্ট এপিআই (`server_backend/api/`), পিএইচপি অ্যাডমিন প্যানেল (`server_backend/admin/`) এবং মাইসিকুয়েল স্কিমা (`server_backend/deenone_db.sql`)** সম্পূর্ণ প্রোডাকশন-রেডি করতে হবে।
- **একক কনফিগারেশন কেন্দ্র (Single Point Configuration):** অ্যাপে কোথাও হার্ডকোডেড সার্ভার লিংক থাকবে না; সব API রিকোয়েস্ট `BackendConfigManager`-এর সেন্ট্রালাইজড Base URL দিয়ে পরিচালিত হবে। ব্যাকএন্ডে সমস্ত ডাটাবেজ কানেকশন `server_backend/config.php`-এর মাধ্যমে হবে।
- **অ্যাডমিন প্যানেল পূর্ণাঙ্গ CRUD ও ব্লকিং:** অ্যাডমিন প্যানেলে ডেটা যুক্ত (Add), সম্পাদনা (Edit), মুছে ফেলা (Delete) এবং নিষ্ক্রিয়/ব্লক (Block/Deactivate) করার পূর্ণ ব্যবস্থা থাকবে।
- **অ্যাপে তাৎক্ষণিক প্রভাব ও রিমুভাল:** অ্যাডমিন প্যানেল থেকে কোনো আইটেম ডিলিট বা ব্লক করা হলে, সিঙ্ক্রোনাইজেশনের মাধ্যমে অ্যান্ড্রয়েড অ্যাপে তা অবিলম্বে হাইড হয়ে যাবে।
- **হাইব্রিড লোকাল ও অনলাইন স্টোরেজ:** ডেটা অনলাইন থেকে সিঙ্ক হয়ে ব্যবহারকারীর লোকাল স্টোরেজে (Room Database/Cache) সংরক্ষিত থাকবে এবং অনলাইন ডাটাবেজের যেকোনো স্টেট পরিবর্তনের সাথে স্বয়ংক্রিয়ভাবে আপডেট থাকবে।
- **বাধ্যতামূলক বিল্ড-টাইম ডাটা ইন্টিগ্রেশন চেক:** প্রতিটি ফিচার ইমপ্লিমেন্টেশন ও বিল্ডের সময় অ্যান্ড্রয়েড ফ্রন্টএন্ড, পিএইচপি ব্যাকএন্ড এপিআই, পিএইচপি অ্যাডমিন প্যানেল এবং মাইসিকুয়েল ডাটাবেজের মধ্যে দ্বিমুখী ডাটা সিঙ্কিং (Auto Import/Export) সক্রিয় ও নির্বিঘ্ন রয়েছে কিনা তা যাচাই করা সম্পূর্ণ বাধ্যতামূলক।

### ১২. শতভাগ নির্ভুল, সময়নিষ্ঠ ও মিসিং-মুক্ত নোটিফিকেশন সিস্টেম (100% Accurate & Timely Notification Engine)
- **একুরেট টাইমিং ও জিরো ডিলে:** প্রতিটি নামাজের ওয়াক্ত (আজান ও এলার্ট), প্রাক-ওয়াক্ত রিমাইন্ডার, সাহরী ও ইফতার অ্যালার্ম, জুমুআ রিমাইন্ডার, তাহাজ্জুদ ও চাশত নফল সালাত, এবং দৈনিক ইসলামিক বার্তা/কুইজ নোটিফিকেশন ১০০% নিখুঁত সময়ে প্রেরিত হতে হবে। কোনো অবস্থাতেই নোটিফিকেশন মিস বা বিলম্ব হওয়া চলবে না।
- **ব্যাটারি অপ্টিমাইজেশন ও ডোজ মোড রেজিলিয়েন্স:** অ্যান্ড্রয়েডের ব্যাকগ্রাউন্ড কিলিং বা ব্যাটারি সেভারের কারণে যেন নোটিফিকেশন বন্ধ না হয়, সেজন্য `AlarmManager.setExactAndAllowWhileIdle()` বা ফোরগ্রাউন্ড সার্ভিস / ওয়েক-লক এবং ব্যাটারি অপ্টিমাইজেশন পারমিশন হ্যান্ডলিং নিশ্চিত করতে হবে।
- **রিবুট ও টাইম-জোন অটো-রিসেডিউলিং:** ডিভাইস রিস্টার্ট বা টাইম জোন / অবস্থান পরিবর্তনের সাথে সাথে `AlarmRescheduler` ও `DailyIslamicReminderScheduler`-এর মাধ্যমে সমস্ত নোটিফিকেশন স্বয়ংক্রিয়ভাবে নির্ভুল সময়ে পুনরায় ক্যালকুলেট ও শিডিউল হতে হবে।
- **ভাষানুযায়ী একুরেট মেসেজ ডেলিভারি:** ব্যবহারকারীর নির্বাচিত ভাষা (বাংলা বা ইংরেজি) অনুযায়ী নোটিফিকেশনের শিরোনাম, টেক্সট ও বিবরণী খাঁটি বাংলা বা খাঁটি ইংরেজিতে ডেলিভারি হবে।

### ১৩. আইকন কন্টেইনার ও বৃত্তাকার শ্যাডো ব্যাকগ্রাউন্ড পলিসি (Universal Circular Icon Background & Shadow)
- **বৃত্তাকার ব্যাকগ্রাউন্ড ও শ্যাডো:** এন্টায়ার অ্যাপের সমস্ত আইকনের পেছনে সুনির্দিষ্ট গোলাকার/বৃত্তাকার ব্যাকগ্রাউন্ড বা সফট শ্যাডো কন্টেইনার (`Circular Background / Soft Shadow / Badge Pill Container`) বাধ্যতামূলক থাকতে হবে।
- **টপিক-রিলেটেড আইকনোগ্রাফি:** প্রতিটি আইকন অবশ্যই সংশ্লিষ্ট বিষয়ের সাথে প্রাসঙ্গিক (Topic-Related) এবং ডার্ক/লাইট মোডে চমৎকার কন্ট্রাস্ট ও স্পষ্ট ভিজিবিলিটি সম্পন্ন হতে হবে।

### ১৪. এন্টায়ার অ্যাপে ইউনিফর্ম বাটন সাইজ ও স্ট্যান্ডার্ডাইজেশন (Uniform Button Size & Dimensions Across App)
- **সুষম ও অভিন্ন বাটন সাইজ:** এন্টায়ার অ্যাপের প্রতিটি স্ক্রিনে ব্যবহৃত সমস্ত অ্যাকশন বাটন, ডায়লগ বাটন ও ইন্টারঅ্যাক্টিভ বাটনের সাইজ, হাইট, প্যাডিং ও ডাইমেনশন একই সুষম স্ট্যান্ডার্ড সাইজের হতে হবে। কোনো বাটনের সাইজ অস্বাভাবিক বড় বা কোনো বাটনের সাইজ ছোট হওয়া সম্পূর্ণ নিষিদ্ধ।

### ১৫. স্থির হেডার আইকন পজিশন ও অভিন্ন বটম নেভিগেশন বার (Fixed Header Icon Alignment & Unified Bottom Navigation)
- **বটম নেভিগেশনের শতভাগ অভিন্নতা:** অ্যাপের প্রতিটি ফাংশন ও পেইজে বটম নেভিগেশন বারের ব্যাকগ্রাউন্ড কালার, আইকন সাইজ, টেক্সট কালার এবং হাইট শতভাগ অভিন্ন থাকবে। ফাংশন পরিবর্তনের সাথে সাথে কালার বা সাইজ পরিবর্তন হওয়া সম্পূর্ণ নিষিদ্ধ।
- **হেডার আইকনের স্থির অবস্থান (Zero Jumping/Shifting):** হোমপেজ, সাইডবার, কমিউনিটি, নোটিফিকেশন বা যেকোনো পেইজে প্রদর্শিত হেডার আইকনসমূহ (সাইডবার ড্রয়ার আইকন, নোটিফিকেশন বেল, প্রোফাইল ইত্যাদি) সবসময় স্ক্রিনের একই নির্দিষ্ট উল্লম্ব উচ্চতা ও পজিশনে (`Exact Same Vertical/Horizontal Alignment & Padding`) স্থির থাকবে। এক পেইজ থেকে অন্য পেইজে যাওয়ার সময় আইকন উপরে-নিচে ওঠানামা করা বা লাফানো সম্পূর্ণ নিষিদ্ধ।

### ১৬. সর্বজনীন ইউনিফর্ম কার্ড ও পঠন সেটিংস স্ট্যান্ডার্ড (Universal Uniform Expandable Card & Reading Settings Standard)
- **হজ ইতিহাস স্ট্যান্ডার্ডের শতভাগ প্রতিফলন:** অ্যাপের সকল তথ্যবহুল, ইতিহাস, পরামর্শ, মাসআলা ও বিষয়ভিত্তিক সেকশনের (Informational, History, Advice, Masail, Topics, Articles) কার্ড ডিজাইন, ফন্ট সাইজ এবং লেআউট হুবহু হজ ইতিহাস (`HajjHistoryPageDialog`) সেকশনের ন্যায় শতভাগ অভিন্ন, দৃষ্টিনন্দন ও কনসিস্টেন্ট হতে হবে।
- **টপ হেডার বার:** ৫৬dp উচ্চতা, ব্যাকগ্রাউন্ড `@color/bg_top_bar`, বামে ব্যাক বাটন (`40dp x 40dp`), সেন্টারে ১৯sp বোল্ড টাইটেল (`@color/text_primary`), এবং ডানে পঠন সেটিংস গিয়ার বাটন (`40dp x 40dp`, `@drawable/ic_settings`, `@color/accent_mint`)।
- **পঠন সেটিংস অপশনস (Reading Settings Menu):** সেটিংস গিয়ার বাটনে ক্লিক করলে ফন্ট সাইজ নির্বাচন (ছোট ১৩ sp, সাধারণ ১৫ sp, প্রমিত ১৭ sp, বড় ১৯ sp), সবগুলো বিস্তারিত/সংক্ষেপ (Expand All / Collapse All), সম্পূর্ণ পাতা কপি ও শেয়ার করার অপশন থাকতে হবে। ব্যবহারকারীর ফন্ট সাইজ চয়েস `SharedPreferences`-এ সংরক্ষিত থাকবে।
- **কার্ডভিউ ডিজাইন ও স্টাইলিং:** ১৬dp কর্নার রেডিয়াস, ১dp বর্ডার (`@color/border_card`), ব্যাকগ্রাউন্ড `@color/bg_card`, নিচে ১২dp মার্জিন (`layout_marginBottom="12dp"`), এবং প্যাডিং ১৬dp।
- **কার্ডের ভেতরের কন্টেন্ট ও ফন্ট সাইজ:**
  - হেডিং/টাইটেল: ১৭sp বোল্ড (`@color/text_primary`)।
  - প্রিভিউ টেক্সট: ১৪.৫sp / ১৫sp বা ডাইনামিক ফন্ট সাইজ (`@color/text_secondary`), সর্বোচ্চ ২ লাইন ও এলিপসাইজ।
  - বিস্তারিত টেক্সট: ডাইনামিক ফন্ট সাইজ, লাইন স্পেসিং ৫dp (`@color/text_primary`), ডিফল্টভাবে সংকুচিত/লুকানো থাকবে।
  - রেফারেন্স/উৎস (প্রযোজ্য ক্ষেত্রে): ১৩sp ইতালিক (`@color/accent_teal`), বিস্তারিত ওপেন হলে নিচে দৃশ্যমান হবে।
  - ইনলাইন এক্সপ্যান্ড/কোল্যাপ্স বাটন: "বিস্তারিত ⌵" / "সংক্ষেপ করুন ∧" (১৪sp বোল্ড `@color/accent_teal` ও ১৮dp ডাউন/আপ শেভরন)।
- **ইনলাইন এক্সপ্যান্ড নীতি (Inline Expand/Collapse):** আলাদা পপআপ ডায়ালগে না গিয়ে কার্ডের ভেতরেই সরাসরি ইনলাইন বিস্তার লাভ করবে। কার্ডে বা বিস্তারিত রো-তে ক্লিক করলে মসৃণভাবে বিস্তারিত লেখা উন্মোচিত হবে।
- **বাটনে টাচ অ্যানিমেশন ও কার্ডে জিরো অ্যানিমেশন (Rule 7 Compliance):** কার্ডভিউতে কোনো সংকোচন বা স্প্রিং ইফেক্ট হবে না। শুধুমাত্র ব্যাক বাটন, সেটিংস বাটন এবং বিস্তারিত টগল বাটনে স্প্রিং অ্যানিমেশন থাকবে।

### ১৭. লাইভ প্রোডাকশন ব্যাকএন্ড ও ডাটাবেজ কনফিগারেশন স্ট্যান্ডার্ড এবং নিরাপত্তা নীতি (Live Production Backend & Database Configuration Standard and Security Policy)
- **স্থায়ী লাইভ ডাটাবেজ ও সার্ভার ক্রেডেনশিয়াল (Permanent Server & Database Credentials):**
  - **ডোমেইন ও বেস ইউআরএল (Domain & Base URL):** `https://deenone.top/`
  - **এপিআই এন্ডপয়েন্ট বেস (API Base URL):** `https://deenone.top/api/`
  - **অ্যাডমিন প্যানেল ইউআরএল (Admin Panel URL):** `https://deenone.top/server_backend/admin/`
  - **ডাটাবেজ নাম (Database Name):** `deenonet_db`
  - **ডাটাবেজ ইউজারনেম (Database Username):** `deenonet_riad`
  - **ডাটাবেজ পাসওয়ার্ড (Database Password):** `@Labib013rt`
  - **ডাটাবেজ হোস্ট (Database Host):** `localhost` (cPanel MySQL)
  - **ডাটাবেজ ক্যারেক্টার সেট (DB Charset):** `utf8mb4` (বাংলা ও আরবি ইউনিকোডের শতভাগ সাপোর্ট)
- **সার্ভার সাইড কনফিগারেশন (`server_backend/config.php`):** সার্ভারের সমস্ত ডাটাবেজ কানেকশন সেন্ট্রালাইজড `server_backend/config.php` ফাইলের মাধ্যমে লাইভ প্রোডাকশন ক্রেডেনশিয়াল ব্যবহার করে পরিচালিত হবে। কোনো সাব-মডিউল বা এপিআই ফাইলে ভিন্ন বা লোকাল ক্রেডেনশিয়াল ব্যবহার সম্পূর্ণ নিষিদ্ধ।
- **সর্বোচ্চ অ্যাপ সিকিউরিটি ও ক্লায়েন্ট-সাইড ক্রেডেনশিয়াল বর্জন (STRICT Zero DB Credentials in Android APK):**
  - অ্যান্ড্রয়েড অ্যাপের (Java/Kotlin/XML) কোনো ফাইলে ডাটাবেজের ইউজারনেম, পাসওয়ার্ড বা সরাসরি MySQL কানেকশন স্ট্রিং রাখা **সম্পূর্ণ ও চিরতরে নিষিদ্ধ**।
  - রিভার্স ইঞ্জিনিয়ারিং বা APK ডিকম্পাইলের মাধ্যমে কেউ যেন ডাটাবেজের ক্রেডেনশিয়াল বের করতে না পারে, সেজন্য অ্যান্ড্রয়েড অ্যাপ শুধুমাত্র সুরক্ষিত HTTPS REST API (`https://deenone.top/api/...` ও `BackendConfigManager`-এর মাধ্যমে) দ্বারা ডাটাবেজের সাথে যোগাযোগ করবে। সমস্ত ডাটাবেজ কোয়েরি ও পাসওয়ার্ড নিরাপদভাবে সার্ভারের ভেতর সুরক্ষিত থাকবে।

### ১৮. বাধ্যতামূলক বিল্ড-টাইম ও কোড ইমপ্লিমেন্টেশন ডাটাবেজ সিঙ্ক, অটো-ইমপোর্ট/এক্সপোর্ট, পিএইচপি অ্যাডমিন কানেক্টিভিটি ও এসকিউএল এরর চেকিং নীতি (Mandatory Build-Time MySQL Connectivity, Auto-Sync Import/Export, PHP Admin Panel Integration & Zero SQL Error Verification Policy)
- **প্রতিটি কোড ইমপ্লিমেন্টেশন ও অ্যাপস বিল্ডে বাধ্যতামূলক যাচাই (Mandatory Check on Every Build & Implementation):**
  - প্রতিবার কোড লেখা, আপডেট করা বা অ্যাপস বিল্ড (`./gradlew assembleDebug`) করার সময় শতভাগ নিশ্চিত করতে হবে যে অ্যাপসের প্রতিটি ফিচার ও মডিউলের সমস্ত ডাটা MySQL ডাটাবেজের সাথে যথাযথভাবে সংযুক্ত (Connected & Synchronized) রয়েছে। কোনো ডাটা যাতে হার্ডকোডেড বা ডাটাবেজ-বিচ্ছিন্ন অবস্থায় না থাকে।
- **স্বয়ংক্রিয় দ্বিমুখী ডাটা সিঙ্ক (Auto-Sync Data Import & Export Verification):**
  - **অটো-সিঙ্ক ডাটা ইমপোর্ট (Auto Data Import):** সার্ভার MySQL ডাটাবেজ থেকে এপিআই-এর মাধ্যমে অ্যান্ড্রয়েড অ্যাপ্লিকেশনে সমস্ত প্রয়োজনীয় ডাটা ফেচিং ও অটো-ইমপোর্ট (লোকাল ক্যাশ/Room Database-এ সংরক্ষণ) সম্পূর্ণ স্বয়ংক্রিয় ও নিখুঁতভাবে কার্যকর হতে হবে।
  - **অটো-সিঙ্ক ডাটা এক্সপোর্ট (Auto Data Export):** ব্যবহারকারীর যেকোনো প্রয়োজনীয় অ্যাকশন, ইনপুট, সেটিংস বা সাবমিশন যেন অ্যাপ থেকে সার্ভার ডাটাবেজে যথাযথ REST API এন্ডপয়েন্টে স্বয়ংক্রিয়ভাবে এক্সপোর্ট/সিঙ্ক হয়।
- **পিএইচপি অ্যাডমিন প্যানেলের সাথে বাধ্যতামূলক নিরবচ্ছিন্ন সংযোগ (PHP Admin Panel Real-Time Sync & Connectivity):**
  - অ্যাপের প্রতিটি ডাইনামিক ডেটা উপাদান পিএইচপি অ্যাডমিন প্যানেলের (`server_backend/admin/`) সাথে শতভাগ কানেক্টেড থাকতে হবে।
  - অ্যাডমিন প্যানেল থেকে কোনো ডেটা যুক্ত (Add), সম্পাদনা (Edit), মুছে ফেলা (Delete) বা ব্লক (Block/Deactivate) করার সাথে সাথে তা তাৎক্ষণিকভাবে অ্যাপে এবং এপিআই রেসপন্সে কার্যকর হচ্ছে কিনা তা প্রতিটি বিল্ড ও কোড চেঞ্জে যাচাই করতে বাধ্য।
- **জিরো এসকিউএল এরর ও স্কিমা ইন্টিগ্রিটি (Strict Zero SQL Error & Schema Validation):**
  - `server_backend/deenone_db.sql` স্কিমা ফাইল, টেবিল স্ট্রাকচার, পিএইচপি এপিআই কোয়েরি এবং অ্যাডমিন প্যানেলের এসকিউএল অপারেশনে কোনো প্রকার সিনট্যাক্স এরর, অমিল কলাম, মিসিং টেবিল বা ফরেন কি ত্রুটি (SQL Error) থাকা সম্পূর্ণ নিষিদ্ধ।
  - ডাটাবেজ স্কিমা বা নতুন ফিল্ডের যেকোনো পরিবর্তনের ক্ষেত্রে এসকিউএল স্টেটমেন্ট সম্পূর্ণ এরর-মুক্ত (`0 SQL Error`) এবং লাইভ ডাটাবেজ (`deenonet_db`) কাঠামোর সাথে সামঞ্জস্যপূর্ণ কিনা তা পুঙ্খানুপুঙ্খভাবে পরীক্ষা করা বাধ্যতামূলক।

### ১৯. বাধ্যতামূলক ১৪-দফা ফুল-স্ট্যাক MySQL ও PHP অ্যাডমিন প্যানেল রিয়েল-টাইম অটো-সিঙ্ক মেট্রিক্স (Mandatory 14-Point Full-Stack MySQL & PHP Admin Real-Time Auto-Sync Matrix)
প্রকল্পের প্রতিটি বিল্ড ও নতুন কোড সংযুক্তির সময় নিম্নোক্ত ১৪টি কোর ফিচারের প্রতিটি ডেটা সরাসরি লাইভ MySQL ডাটাবেজ (`deenonet_db`) এবং পিএইচপি অ্যাডমিন প্যানেলের (`server_backend/admin/`) সাথে শতভাগ সংযুক্ত, অটো-সাইনিং এবং রিয়েল-টাইমে কার্যকর থাকা বাধ্যতামূলক:
1. **কমিউনিটি ট্যাব (Community Tab):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/community_feed.php` | টেবিল: `community_posts`, `community_likes`, `community_reports`
   - **ফাংশনালিটি:** ব্যবহারকারীদের পোস্ট, লাইক ও কমেন্ট সরাসরি ডাটাবেজে রিয়েল-টাইম সংরক্ষণ ও প্রদর্শন। অ্যাডমিন প্যানেল (`admin/community.php`) থেকে পোস্ট পর্যবেক্ষণ, অনুমোদন বা ডিলিট করার সাথে সাথে অ্যাপে তাৎক্ষণিক প্রতিফলন।
2. **র‍্যাংক ও লিডারবোর্ড (Rank / Leaderboard Section):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/leaderboard.php`, `api/quiz_leaderboard.php` | টেবিল: `users`
   - **ফাংশনালিটি:** ব্যবহারকারীদের সংগৃহীত সর্বোচ্চ পয়েন্ট, র‍্যাংক, এক্সপি ও অর্জিত সম্মাননা অনুযায়ী ডাটাবেজ থেকে রিয়েল-টাইম লিডারবোর্ড ফেচ ও প্রদর্শন।
3. **আমল পয়েন্ট কালেকশন (Amal Points Collection):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/user_sync.php` | টেবিল: `users.total_points`
   - **ফাংশনালিটি:** ব্যবহারকারী যখনই কোনো দৈনিক আমল সম্পন্ন করবেন, তাৎক্ষণিকভাবে অর্জিত পয়েন্ট স্বয়ংক্রিয়ভাবে ক্লাউড ডাটাবেজে সিঙ্ক ও ব্যালেন্সে যুক্ত হবে।
4. **সালাত ও ক্বাযা নামাজ ট্র্যাকার (Salah & Qaza Prayer Tracker):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/qaza.php` | টেবিল: `user_qaza_prayers`
   - **ফাংশনালিটি:** ইউজার কবে কয় ওয়াক্ত নামাজ পড়েছেন এবং কত ওয়াক্ত ক্বাযা রয়েছে তা স্বয়ংক্রিয়ভাবে ডাটাবেজের সাথে সংযুক্ত ও টু-ওয়ে সিঙ্ক হবে।
5. **আমল ট্র্যাকার (Amal Tracker):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/user_sync.php` | টেবিল: `users.daily_streak`, `user_amal_records`
   - **ফাংশনালিটি:** দৈনিক আমল চেকলিস্ট, স্ট্রিক ও ধারাবাহিকতা স্বয়ংক্রিয়ভাবে ডাটাবেজের সাথে সিঙ্ক হবে যাতে ডিভাইস পরিবর্তন করলেও আমলের হিসেব সুরক্ষিত থাকে।
6. **নলেজ ব্যাটল (Knowledge Battle - Multiplayer Real-Time):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/create_room.php`, `api/join_room.php`, `api/get_room.php`, `api/submit_answer.php` | টেবিল: `battle_rooms`, `battle_players`, `battle_answers`
   - **ফাংশনালিটি:** স্বয়ংক্রিয় অটো-সাইনিং, রিয়েল-টাইম রুম তৈরি ও জয়েনিং, লাইভ প্লেয়ারদের সাথে কুইজ প্রতিযোগিতা এবং লাইভ স্কোরিং ডাটাবেজের মাধ্যমে নিরবচ্ছিন্নভাবে পরিচালিত হবে।
7. **কুইজ সেকশন (Quiz Section):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/get_quiz_categories.php`, `api/get_quiz_questions.php`, `api/quiz_leaderboard.php` | টেবিল: `quiz_categories`, `quiz_questions`, `user_quiz_results`
   - **ফাংশনালিটি:** অ্যাডমিন প্যানেল (`admin/quiz_questions.php`) থেকে কুইজের ক্যাটাগরি ও প্রশ্ন রিয়েল-টাইমে লোড হবে এবং ফলাফল অনুযায়ী পয়েন্ট সিস্টেম ডাটাবেজে তাৎক্ষণিক জমা হবে।
8. **হাদিস সেকশন (Hadith Section):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/get_hadith_categories.php`, `api/get_hadith_chapters.php`, `api/get_chapter_hadiths.php` | টেবিল: `hadith_books`, `hadith_chapters`, `hadiths`
   - **ফাংশনালিটি:** সমস্ত সহীহ হাদিস গ্রন্থ, অধ্যায় ও হাদিসসমূহ পিএইচপি অ্যাডমিন ও MySQL ডাটাবেজের সাথে শতভাগ কানেক্টেড থাকবে এবং অফলাইন ক্যাশের সাথে অটো-সিঙ্ক হবে।
9. **ইসলামিক বই সেকশন (Islamic Books Section):**
   - **এন্ডপয়েন্ট ও টেবিল:** `api/get_islamic_books.php` | টেবিল: `islamic_books`
   - **ফাংশনালিটি:** নির্ভরযোগ্য কিতাবসমূহের তালিকা ও পিডিএফ লিংক পিএইচপি অ্যাডমিন প্যানেল (`admin/islamic_books.php`) এবং MySQL ডাটাবেজ থেকে রিয়েল-টাইমে লোড ও ডাউনলোড হবে।
10. **দোয়া ভান্ডার (Dua Hub / Dua Vandar):**
    - **এন্ডপয়েন্ট ও টেবিল:** `api/get_duas.php` | টেবিল: `duas`, `dua_categories`, `user_dua_favorites`
    - **ফাংশনালিটি:** সমস্ত প্রামাণ্য মাসনূন দোয়া, অর্থ, রেফারেন্স ও অডিও পিএইচপি অ্যাডমিন প্যানেল (`admin/duas.php`) ও MySQL ডাটাবেজের সাথে শতভাগ সংযুক্ত থাকবে।
11. **পূর্ণাঙ্গ প্রোফাইল (Full User Profile):**
    - **এন্ডপয়েন্ট ও টেবিল:** `api/auth.php`, `api/user_sync.php` | টেবিল: `users`
    - **ফাংশনালিটি:** ব্যবহারকারীর নাম, ফোন, ইমেইল, রক্তের গ্রুপ, জেলা, প্রোফাইল পিকচার, অর্জিত মোট পয়েন্ট ও স্ট্যাটাস ক্লাউড ডাটাবেজে সুরক্ষিত থাকবে এবং যেকোনো ডিভাইসে লগইনে তাৎক্ষণিক পুনরুদ্ধার হবে।
12. **রক্তদান নেটওয়ার্ক (Blood Donation Network):**
    - **এন্ডপয়েন্ট ও টেবিল:** `api/blood_donors.php` | টেবিল: `blood_donors`, `blood_requests`
    - **ফাংশনালিটি:** রক্তদাতা হিসেবে নিবন্ধন, জেলা ও গ্রুপের ভিত্তিতে ফিল্টারিং এবং জরুরি রক্তের রিকোয়েস্ট পিএইচপি অ্যাডমিন ও MySQL ডাটাবেজের সাথে সরাসরি কানেক্টেড থাকবে।
13. **অ্যাপের ভাষা ও থিম সেটিংস সিঙ্ক (App Language & Theme Settings Sync):**
    - **এন্ডপয়েন্ট ও টেবিল:** `api/user_sync.php` | টেবিল: `users.app_language`, `users.theme_mode`
    - **ফাংশনালিটি:** ব্যবহারকারী যখনই ভাষা পরিবর্তন (বাংলা/ইংরেজি) বা থিম মোড পরিবর্তন (ডার্ক/লাইট) করবেন, তা সাথে সাথে ডাটাবেজে সিঙ্ক হবে এবং পরবর্তীতে লগইন করলে স্বয়ংক্রিয়ভাবে তার পছন্দের সেটিংস লোড হবে।
14. **অ্যাডমিন প্যানেল নোটিফিকেশন (Admin Push & Broadcast Notifications):**
    - **এন্ডপয়েন্ট ও টেবিল:** `api/get_notifications.php` | টেবিল: `push_notifications`, `app_notices`
    - **ফাংশনালিটি:** পিএইচপি অ্যাডমিন প্যানেল (`admin/notifications.php`) থেকে কোনো নোটিফিকেশন বা ইসলামিক বার্তা প্রেরণ করা মাত্রই তা অ্যাপে রিয়েল-টাইমে শো করবে।

---

### ২০. ইউজারের প্রতিটি অ্যাক্টিভিটি (নামাজ আদায়/কাযা, কুইজ পয়েন্ট, আমল ট্র্যাকার, সেটিংস পরিবর্তন) রিয়েল-টাইমে MySQL ডেটাবেজে সংরক্ষণ এবং লগআউট/নতুন ডিভাইসে লগইনকালে শতভাগ অটো-রিস্টোর ও সিঙ্ক নীতি (Mandatory Real-Time User Activity Persistence & Cross-Device Cloud Sync Policy)
> **সর্বকালীন বাধ্যতামূলক নিয়ম:** ব্যবহারকারীর প্রতিটি ধর্মীয় ও অ্যাপ অ্যাক্টিভিটি—যেমন:
> ১. **নামাজ আদায় ট্র্যাকিং (Salah Tracking):** ইউজার কোন ওয়াক্ত নামাজ পড়লেন কি পড়লেন না (ফজর, যোহর, আসর, মাগরিব, এশা, তাহাজ্জুদ—জামাত, একাকী, দেরী, কাযা)।
> ২. **কাযা নামাজ ট্র্যাকার (Qaza Tracker):** জীবনের কাযা হিসাব, কাযা আদায় করা বা কাযা বৃদ্ধি পাওয়া।
> ৩. **কুইজ ও আমল পয়েন্ট (Quiz Points & Amal Records):** কুইজ খেলে পয়েন্ট অর্জন, স্কোরবোর্ড, দৈনিক আমল চেকলিস্ট, স্ট্রিক সংখ্যা ও এক্সপি।
> ৪. **অ্যাপ ও ইউজার সেটিংস (User Settings & Preferences):** অ্যাপের ভাষা (bn/en), থিম মোড (dark/light), সালাত ক্যালকুলেশন মেথড, মাযহাব/জুরিস্টিক মেথড, আযান ও নোটিফিকেশন সেটিংস বা অফসেট সমন্বয়।
> 
> প্রতিটি পরিবর্তন ঘটার সাথে সাথে ব্যাকগ্রাউন্ডে নন-ব্লকিং অ্যাসিনক্রোনাস থ্রেডে রিয়েল-টাইমে MySQL ডেটাবেজে (`users`, `user_prayer_logs`, `user_qaza_records`, `user_amal_logs`) স্বয়ংক্রিয়ভাবে সংরক্ষিত (Auto-Saved) হতে হবে।
> 
> **ক্রস-ডিভাইস রিস্টোর ও ইমপোর্ট পলিসি (Cross-Device Auto-Restore on Login):**
> ব্যবহারকারী যখনই অ্যাপ থেকে লগআউট করবেন এবং পরবর্তীতে পুনরায় সেই ডিভাইসে কিংবা সম্পূর্ণ ভিন্ন কোনো অ্যান্ড্রয়েড ডিভাইসে লগইন করবেন, অ্যাপ তাৎক্ষণিকভাবে ব্যাকএন্ডের `api/user_sync.php?action=full_restore` এপিআই-এর মাধ্যমে তার পূর্ববর্তী সমস্ত নামাজ ট্র্যাকিং হিস্ট্রি, কাযা কাউন্ট, কুইজ পয়েন্ট, আমল ডাটা এবং সকল ব্যক্তিগত সেটিংস স্বয়ংক্রিয়ভাবে ইম্পোর্ট (Restore) করে লোকাল ডাটাবেজ ও অ্যাপ সেটিংসে পুশ করবে। কোনো ডাটা হারানো যাবে না এবং কোনো ডেটাবেজ/এসকিউএল এরর থাকা সম্পূর্ণ নিষিদ্ধ।

---

### ২৪. কোড বিশুদ্ধতা, পেশাদারিত্ব ও এআই-রেফারেন্স বর্জন নীতি (Mandatory Code Authenticity & Professional Production Quality Rule)

> **MANDATORY CODE AUTHENTICITY & PROJECT QUALITY RULE:**
> 
> The entire project MUST be written and maintained as clean, professional, human-readable production code.
> 
> No part of the Android Java application, PHP backend, MySQL database, API, configuration, documentation, comments, variable naming, file structure, metadata, or generated output should contain unnecessary references to AI tools, AI generation, prompts, models, ChatGPT, OpenAI, or any other AI system.
> 
> **MANDATORY RULES:**
> 
> 1. **Do NOT add comments such as:**
>    - Generated by AI
>    - Generated by ChatGPT
>    - Generated by OpenAI
>    - AI-generated code
>    - Created by AI
>    - Any similar AI attribution
> 
> 2. **Do NOT add AI-related metadata, hidden markers, signatures, tags, filenames, database fields, API fields, or configuration values** unless explicitly required by the application.
> 
> 3. **Java, PHP, JavaScript, SQL, HTML, CSS and other source code MUST follow consistent professional coding conventions.**
> 
> 4. **Variable, function, class, method, database table, column and API names MUST be meaningful and relevant to their actual purpose.**
> 
> 5. **Do NOT use strange, random, unnecessarily verbose, repetitive, or unnatural naming** simply because code was generated automatically.
> 
> 6. **Do NOT add unnecessary comments.** Comments should only explain genuinely important logic, business rules, security considerations, or non-obvious implementation details.
> 
> 7. **Do NOT add unnecessary documentation, placeholder text, TODO statements, debug messages, test text, or development artifacts.**
> 
> 8. **The PHP backend, Java Android application, MySQL database and API architecture MUST remain consistent with one another.**
> 
> 9. **Database tables, columns, relationships, indexes, constraints and queries MUST be designed according to normal professional database practices.**
> 
> 10. **API request and response structures MUST remain clean, consistent, predictable and production-ready.**
> 
> 11. **Source files MUST contain only code and resources that are actually required by the application.**
> 
> 12. **Remove unused imports, variables, functions, classes, files, database fields, API endpoints, assets and dependencies.**
> 
> 13. **Do NOT create artificial complexity merely to make the code appear manually written.**
> 
> 14. **Do NOT intentionally introduce mistakes, inconsistent formatting, poor coding practices, bugs, or unnecessary complexity to disguise how the code was produced.**
> 
> 15. **All code MUST be reviewed for correctness, security, performance, maintainability and consistency before being considered complete.**
> 
> 16. **The final project MUST look and behave like a professionally maintained production software project, regardless of which tools were used during development.**
> 
> 17. **No hidden AI-detection bypass mechanism, watermark, fingerprint, obfuscation, or artificial modification may be added.**
> 
> ---
> 
> ### 🛑 চূড়ান্ত বাধ্যতামূলক নিয়ম (FINAL MANDATORY RULE):
> 
> Focus on producing genuinely high-quality, maintainable, professional software rather than attempting to manipulate or bypass AI-detection systems.
> 
> The final Java application, PHP backend, MySQL database, APIs and project files MUST contain no unnecessary AI references, attribution, metadata, artifacts, or development remnants.
> 
> This rule applies to the ENTIRE project and to every future modification.

---

### ২৫. সর্বোচ্চ বাধ্যতামূলক নিরাপত্তা ও অ্যান্টি-হ্যাকিং স্থায়ী পরিচালনা নিয়মাবলী (Mandatory Security & Anti-Hacking Rule)

> **MANDATORY SECURITY & ANTI-HACKING RULE:**
> 
> The entire DeenOne system MUST be developed with security as a first-class requirement.
> 
> This applies to the complete system:
> - Android Java application
> - PHP backend
> - REST API
> - MySQL database
> - Admin Panel
> - Authentication
> - File storage
> - Image/audio/PDF storage
> - User data
> - Server configuration
> - API communication
> - Ad configuration
> - Payment or sensitive operations
> - Third-party integrations
> 
> The system MUST follow secure-by-default principles and MUST NOT assume that the Android application, API requests, or client-side validation can be trusted.
> 
> #### ১. PHP SECURITY
> - Use prepared statements / parameterized queries for ALL database queries.
> - NEVER concatenate user input directly into SQL queries.
> - Validate and sanitize all incoming data server-side.
> - Use strict input validation and appropriate data types.
> - Implement secure output encoding to prevent XSS.
> - Prevent SQL Injection, XSS, CSRF, SSRF, command injection, path traversal, file inclusion, session attacks and other common web vulnerabilities.
> - Disable unnecessary PHP functions and server features where appropriate.
> - Never expose PHP errors, stack traces, database errors, file paths, credentials, or internal server information to users in production.
> - Use secure exception and error handling.
> - Keep PHP and all server-side dependencies updated.
> 
> #### ২. API SECURITY
> - Every sensitive API endpoint MUST have proper authentication and authorization.
> - Authentication MUST NOT be based only on values supplied by the client.
> - Verify authorization on the server for every protected operation.
> - Users MUST only be able to access data they are authorized to access.
> - Admin endpoints MUST have separate and stronger authorization.
> - Never trust user IDs, roles, permissions, prices, points, balances, subscription status, or similar values received from the Android app.
> - Sensitive values MUST be verified server-side.
> - Implement rate limiting for authentication, OTP, password reset, sensitive APIs, and other abuse-prone endpoints.
> - Implement appropriate request size limits and timeout controls.
> - Reject malformed, unexpected, or unauthorized requests.
> 
> #### ৩. AUTHENTICATION & SESSION SECURITY
> - Passwords MUST NEVER be stored as plain text.
> - Use a strong password hashing algorithm such as Argon2id or an appropriately configured secure password hashing mechanism.
> - Authentication tokens MUST be securely generated and validated.
> - Tokens MUST have appropriate expiration and revocation mechanisms.
> - Sessions MUST be invalidated after logout where applicable.
> - Password reset tokens MUST be random, short-lived, single-use, and securely stored.
> - OTP systems MUST have expiration, attempt limits, and rate limiting.
> - Do NOT reveal whether an email or account exists where doing so would create unnecessary account enumeration risk.
> - Implement appropriate brute-force protection.
> 
> #### ৪. ADMIN PANEL SECURITY
> - Admin authentication MUST be mandatory.
> - Implement role-based access control.
> - Verify authorization server-side.
> - Never rely on hidden buttons or frontend restrictions for security.
> - Sensitive admin actions SHOULD require additional verification where appropriate.
> - Protect admin sessions securely.
> - Implement rate limiting and brute-force protection.
> - Record important administrative actions in an audit log.
> - Never expose database credentials through the Admin Panel.
> - Never expose server secrets through the Admin Panel.
> 
> #### ৫. MYSQL DATABASE SECURITY
> - Use prepared statements for all queries.
> - Use a dedicated database user with only the permissions actually required by the application.
> - Do NOT use the MySQL root account for the production application.
> - Database credentials MUST NOT be stored inside the Android app.
> - Database credentials MUST NOT be exposed through APIs.
> - Restrict database network access so it is not publicly accessible unless absolutely required.
> - Use proper indexes, constraints, foreign keys, and data types.
> - Protect sensitive database fields appropriately.
> - Perform secure backups. Backup files MUST also be protected from unauthorized access.
> 
> #### ৬. ANDROID JAVA SECURITY
> - Never place MySQL credentials, PHP Admin credentials, private API secrets, private keys, or other server secrets inside the APK.
> - Assume that anything inside the APK can potentially be extracted.
> - Client-side validation MUST NOT be considered a security boundary. Perform important validation and authorization on the server.
> - Use HTTPS/TLS for all production API communication.
> - Do NOT transmit passwords, tokens, or sensitive data over HTTP.
> - Do NOT disable TLS certificate validation.
> - Do NOT accept invalid or self-signed certificates in production.
> - Store sensitive local information using Android's secure storage mechanisms where appropriate.
> - Avoid storing sensitive information in plain text logs, files, SharedPreferences, or local databases.
> - Remove debug logging containing sensitive information from production.
> - Disable or restrict debugging features in production builds.
> 
> #### ৭. API KEY & SECRET PROTECTION
> NEVER hard-code the following inside the Android application:
> - Database passwords
> - PHP Admin credentials
> - Private API keys
> - Server passwords
> - JWT signing secrets
> - Encryption master keys
> - Private third-party credentials
> - Cloud service secrets
> 
> Public identifiers that are specifically designed to be public may be used in the application, but sensitive credentials MUST remain server-side.
> 
> #### ৮. FILE UPLOAD SECURITY
> For image, audio, PDF and other file uploads:
> - Validate file type server-side.
> - Validate MIME type and file signature where appropriate.
> - Validate file size.
> - Generate safe server-side filenames.
> - Prevent executable files from being uploaded where they are not required.
> - Prevent path traversal.
> - Do not trust the original filename.
> - Store uploaded files outside executable server locations where appropriate.
> - Protect private files with authorization checks.
> - Do not allow uploaded files to execute as server-side code.
> 
> #### ৯. FILE & STORAGE SECURITY
> All private resources MUST require appropriate authorization. Do NOT expose sensitive directories such as:
> - Database backups
> - Configuration files
> - Environment files
> - Logs
> - Source code
> - Server secrets
> - Private user documents
> 
> Directory listing MUST be disabled where appropriate.
> 
> #### ১০. CORS & HTTP SECURITY
> Configure CORS strictly. Do NOT use unrestricted `Access-Control-Allow-Origin: *` for authenticated or sensitive APIs unless there is a specific, reviewed reason. Configure appropriate HTTP security headers, including protections against common browser-based attacks where applicable.
> 
> #### ১১. CSRF PROTECTION
> All state-changing web requests that use cookie-based authentication MUST have appropriate CSRF protection. Do NOT assume that authentication alone prevents CSRF.
> 
> #### ১২. RATE LIMITING & ABUSE PROTECTION
> Implement appropriate rate limits for: Login, Registration, OTP, Password reset, API requests, Admin login, Sensitive operations, File uploads, Search where abuse is possible, and other high-risk endpoints. Repeated abusive requests MUST be handled safely without crashing the server.
> 
> #### ১৩. AUTHORIZATION
> - Authentication answers: "Who is this user?"
> - Authorization answers: "Is this user allowed to perform this action?"
> - Both MUST be implemented correctly. Never assume that a logged-in user is automatically authorized to access another user's data.
> 
> #### ১৪. IDOR / ACCESS CONTROL
> Prevent users from accessing another user's resources by simply changing an ID in: URL, API parameter, Request body, Database identifier, or File path. Every protected resource MUST be authorized server-side.
> 
> #### ১৫. SECURITY LOGGING
> Log important security events such as:
> - Failed admin login attempts
> - Successful admin login
> - Password changes
> - Account deletion
> - Permission changes
> - Important configuration changes
> - Suspicious API activity
> 
> Do NOT log passwords, private tokens, authentication secrets, or unnecessary sensitive personal information.
> 
> #### ১৬. PRODUCTION CONFIGURATION
> Production MUST:
> - Disable debug mode.
> - Disable verbose error output.
> - Remove test credentials.
> - Remove development endpoints.
> - Remove mock authentication.
> - Remove test API endpoints.
> - Remove temporary files.
> - Remove unnecessary server modules.
> - Use secure environment configuration.
> - Use HTTPS.
> 
> #### ১৭. DEPENDENCY SECURITY
> Before adding any PHP, Java, Android, or third-party dependency:
> - Verify that it is necessary.
> - Use a maintained version.
> - Review known security vulnerabilities.
> - Avoid abandoned libraries.
> - Keep dependencies updated.
> 
> #### ১৮. BACKEND TRUST MODEL
> The Android application MUST be treated as an untrusted client. A malicious user may modify the APK, intercept requests on a compromised device, manipulate parameters, replay requests, or attempt to call APIs directly. Therefore:
> **IMPORTANT BUSINESS LOGIC MUST ALWAYS BE ENFORCED SERVER-SIDE.**
> Examples include:
> - User permissions
> - Admin permissions
> - Points
> - Rewards
> - Quiz scores
> - Subscription status
> - Account status
> - Download permissions
> - Configuration changes
> - Any financial or sensitive operation
> 
> #### ১৯. SECURITY AGAINST COMMON ATTACKS
> The implementation MUST be designed to mitigate applicable OWASP security risks, including:
> SQL Injection, XSS, CSRF, Broken Access Control, Authentication failures, Security misconfiguration, SSRF, Path Traversal, Insecure File Upload, Sensitive Data Exposure, Rate-limit abuse, Session attacks, API abuse, Injection attacks.
> 
> #### ২০. SECURITY TESTING
> Before production release, perform a security review of: Android application, PHP source code, API endpoints, Admin Panel, MySQL database, Authentication, Authorization, File uploads, File storage, API configuration, Server configuration, Third-party SDKs, Dependencies. Any identified critical or high-risk security vulnerability MUST be fixed before production release.
> 
> ---
> 
> ### 🛑 সর্বপ্রধান স্থায়ী নিরাপত্তা নীতি (FINAL MANDATORY SECURITY RULE):
> - **NEVER assume that the application is secure simply because the UI, Android code, PHP code, or database is hidden.**
> - **The Android application, PHP backend, API, Admin Panel, and MySQL database MUST be designed as separate security boundaries.**
> - **Security validation MUST be performed server-side.**
> - **The system MUST follow secure coding practices and applicable OWASP recommendations.**
> 
> ⚡ **NO plaintext passwords.**  
> ⚡ **NO exposed database credentials.**  
> ⚡ **NO private server secrets inside the APK.**  
> ⚡ **NO direct SQL with user input.**  
> ⚡ **NO unrestricted sensitive API access.**  
> ⚡ **NO client-only authorization.**  
> ⚡ **NO production debug mode.**  
> ⚡ **NO unnecessary exposed endpoints.**  
> ⚡ **NO unnecessary permissions.**  
> ⚡ **NO insecure HTTP communication.**  
> 
> **Security is MANDATORY for every current and future feature.**





