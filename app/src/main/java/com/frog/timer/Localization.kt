package com.frog.timer

import androidx.compose.runtime.Composable
import java.util.Locale

enum class AppLanguage(val code: String, val displayName: String) {
    SYSTEM("system", "System Default"),
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    GERMAN("de", "Deutsch"),
    ITALIAN("it", "Italiano"),
    PORTUGUESE("pt", "Português"),
    RUSSIAN("ru", "Русский"),
    CHINESE("zh", "中文"),
    JAPANESE("ja", "日本語"),
    KOREAN("ko", "한국어"),
    ARABIC("ar", "العربية"),
    HINDI("hi", "हिन्दी"),
    BENGALI("bn", "বাংলা"),
    TURKISH("tr", "Türkçe"),
    VIETNAMESE("vi", "Tiếng Việt"),
    POLISH("pl", "Polski"),
    DUTCH("nl", "Nederlands"),
    THAI("th", "ไทย"),
    INDONESIAN("id", "Bahasa Indonesia"),
    GREEK("el", "Ελληνικά"),
    HEBREW("he", "עברית"),
    SWEDISH("sv", "Svenska"),
    NORWEGIAN("no", "Norsk"),
    DANISH("da", "Dansk"),
    FINNISH("fi", "Suomi")
}

val Translations = mapOf(
    "en" to mapOf(
        "universal_timer" to "Universal Timer", "timer" to "Timer", "stopwatch" to "Stopwatch", 
        "world" to "World", "settings" to "Settings", "theme" to "Theme", 
        "dark_mode" to "Dark Mode", "alarm_sound" to "Alarm Sound", "language" to "Language", 
        "reset" to "Reset", "start" to "Start", "pause" to "Pause", "custom" to "Custom", 
        "lap" to "Lap", "done" to "Done", "cancel" to "Cancel", "ok" to "OK", 
        "select_cities" to "Select Cities", "min" to "Min", "sec" to "Sec", 
        "add_city" to "Add City", "system_default" to "System Default", "close" to "Close"
    ),
    "es" to mapOf(
        "universal_timer" to "Temporizador Universal", "timer" to "Temporizador", "stopwatch" to "Cronómetro",
        "world" to "Mundo", "settings" to "Ajustes", "theme" to "Tema",
        "dark_mode" to "Modo Oscuro", "alarm_sound" to "Sonido", "language" to "Idioma",
        "reset" to "Reiniciar", "start" to "Iniciar", "pause" to "Pausa", "custom" to "Personalizado",
        "lap" to "Vuelta", "done" to "Hecho", "cancel" to "Cancelar",
        "ok" to "Aceptar", "select_cities" to "Villes", "min" to "Min", "sec" to "Seg",
        "add_city" to "Añadir Ciudad", "system_default" to "Sistema", "close" to "Cerrar"
    ),
    "fr" to mapOf(
        "universal_timer" to "Minuteur Universel", "timer" to "Minuteur", "stopwatch" to "Chronomètre",
        "world" to "Monde", "settings" to "Paramètres", "theme" to "Thème",
        "dark_mode" to "Mode Sombre", "alarm_sound" to "Alarme", "language" to "Langue",
        "reset" to "Réinitialiser", "start" to "Démarrer", "pause" to "Pause", "custom" to "Perso",
        "lap" to "Tour", "done" to "Terminé", "cancel" to "Annuler",
        "ok" to "OK", "select_cities" to "Choisir Villes", "min" to "Min", "sec" to "Sec",
        "add_city" to "Ajouter", "system_default" to "Système", "close" to "Fermer"
    ),
    "de" to mapOf(
        "universal_timer" to "Universal-Timer", "timer" to "Timer", "stopwatch" to "Stoppuhr",
        "world" to "Welt", "settings" to "Einstellungen", "theme" to "Design",
        "dark_mode" to "Dunkelmodus", "alarm_sound" to "Alarmton", "language" to "Sprache",
        "reset" to "Zurücksetzen", "start" to "Start", "pause" to "Pause", "custom" to "Eigene",
        "lap" to "Runde", "done" to "Fertig", "cancel" to "Abbrechen",
        "ok" to "OK", "select_cities" to "Städte wählen", "min" to "Min", "sec" to "Sek",
        "add_city" to "Stadt hinzufügen", "system_default" to "System", "close" to "Schließen"
    ),
    "it" to mapOf(
        "universal_timer" to "Timer Universale", "timer" to "Timer", "stopwatch" to "Cronometro",
        "world" to "Mondo", "settings" to "Impostazioni", "theme" to "Tema",
        "dark_mode" to "Modalità Scura", "alarm_sound" to "Sveglia", "language" to "Lingua",
        "reset" to "Reset", "start" to "Inizia", "pause" to "Pausa", "custom" to "Pers.",
        "lap" to "Giro", "done" to "Fatto", "cancel" to "Annulla",
        "ok" to "OK", "select_cities" to "Scegli Città", "min" to "Min", "sec" to "Sec",
        "add_city" to "Aggiungi", "system_default" to "Sistema", "close" to "Chiudi"
    ),
    "pt" to mapOf(
        "universal_timer" to "Temporizador Universal", "timer" to "Timer", "stopwatch" to "Cronômetro",
        "world" to "Mundo", "settings" to "Ajustes", "theme" to "Tema",
        "dark_mode" to "Modo Escuro", "alarm_sound" to "Alarme", "language" to "Idioma",
        "reset" to "Reset", "start" to "Iniciar", "pause" to "Pausa", "custom" to "Perso",
        "lap" to "Volta", "done" to "Pronto", "cancel" to "Cancelar",
        "ok" to "OK", "select_cities" to "Cidades", "min" to "Min", "sec" to "Seg",
        "add_city" to "Adicionar", "system_default" to "Sistema", "close" to "Fechar"
    ),
    "ru" to mapOf(
        "universal_timer" to "Универсальный Таймер", "timer" to "Таймер", "stopwatch" to "Секундомер",
        "world" to "Мир", "settings" to "Настройки", "theme" to "Тема",
        "dark_mode" to "Темная тема", "alarm_sound" to "Звук", "language" to "Язык",
        "reset" to "Сброс", "start" to "Старт", "pause" to "Пауза", "custom" to "Свой",
        "lap" to "Круг", "done" to "Готово", "cancel" to "Отмена",
        "ok" to "ОК", "select_cities" to "Выбор городов", "min" to "Мин", "sec" to "Сек",
        "add_city" to "Добавить", "system_default" to "Система", "close" to "Закрыть"
    ),
    "zh" to mapOf(
        "universal_timer" to "通用计时器", "timer" to "计时器", "stopwatch" to "秒表",
        "world" to "世界时间", "settings" to "设置", "theme" to "主题",
        "dark_mode" to "深色模式", "alarm_sound" to "警报声", "language" to "语言",
        "reset" to "重置", "start" to "开始", "pause" to "暂停", "custom" to "自定义",
        "lap" to "计圈", "done" to "完成", "cancel" to "取消",
        "ok" to "确定", "select_cities" to "选择城市", "min" to "分", "sec" to "秒",
        "add_city" to "添加城市", "system_default" to "系统默认", "close" to "关闭"
    ),
    "ja" to mapOf(
        "universal_timer" to "ユニバーサルタイマー", "timer" to "タイマー", "stopwatch" to "ストップウォッチ",
        "world" to "世界時計", "settings" to "設定", "theme" to "テーマ",
        "dark_mode" to "ダークモード", "alarm_sound" to "アラーム音", "language" to "言語",
        "reset" to "リセット", "start" to "スタート", "pause" to "一時停止", "custom" to "カスタム",
        "lap" to "ラップ", "done" to "完了", "cancel" to "キャンセル",
        "ok" to "OK", "select_cities" to "都市を選択", "min" to "分", "sec" to "秒",
        "add_city" to "都市を追加", "system_default" to "システム設定", "close" to "閉じる"
    ),
    "ko" to mapOf(
        "universal_timer" to "유니버설 타이머", "timer" to "타이머", "stopwatch" to "스톱워치",
        "world" to "세계 시계", "settings" to "설정", "theme" to "테마",
        "dark_mode" to "다크 모드", "alarm_sound" to "알람 소리", "language" to "언어",
        "reset" to "초기화", "start" to "시작", "pause" to "일시정지", "custom" to "사용자 정의",
        "lap" to "랩", "done" to "완료", "cancel" to "취소",
        "ok" to "확인", "select_cities" to "도시 선택", "min" to "분", "sec" to "초",
        "add_city" to "도시 추가", "system_default" to "시스템 기본값", "close" to "닫기"
    ),
    "ar" to mapOf(
        "universal_timer" to "المؤقت العالمي", "timer" to "مؤقت", "stopwatch" to "ساعة توقيت",
        "world" to "العالم", "settings" to "الإعدادات", "theme" to "المظهر",
        "dark_mode" to "الوضع الداكن", "alarm_sound" to "صوت المنبه", "language" to "اللغة",
        "reset" to "إعادة تعيين", "start" to "بدء", "pause" to "إيقاف مؤقت", "custom" to "مخصص",
        "lap" to "دورة", "done" to "تم", "cancel" to "إلغاء",
        "ok" to "موافق", "select_cities" to "اختر المدن", "min" to "دقيقة", "sec" to "ثانية",
        "add_city" to "إضافة مدينة", "system_default" to "تلقائي", "close" to "إغلاق"
    ),
    "hi" to mapOf(
        "universal_timer" to "यूनिवर्सल टाइमर", "timer" to "टाइमर", "stopwatch" to "स्टॉपवॉच",
        "world" to "दुनिया", "settings" to "सेटिंग्स", "theme" to "थीम",
        "dark_mode" to "डार्क मोड", "alarm_sound" to "अलार्म ध्वनि", "language" to "भाषा",
        "reset" to "रीसेट", "start" to "शुरू", "pause" to "विराम", "custom" to "कस्टम",
        "lap" to "लैप", "done" to "हो गया", "cancel" to "रद्द करें",
        "ok" to "ठीक है", "select_cities" to "शहर चुनें", "min" to "मिनट", "sec" to "सेकंड",
        "add_city" to "शहर जोड़ें", "system_default" to "सिस्टम डिफ़ॉल्ट", "close" to "बंद करें"
    ),
    "bn" to mapOf(
        "universal_timer" to "ইউনিভার্সাল টাইমার", "timer" to "টাইমার", "stopwatch" to "স্টপওয়াচ",
        "world" to "বিশ্ব", "settings" to "সেটিংস", "theme" to "থিম",
        "dark_mode" to "ডার্ক মোড", "alarm_sound" to "অ্যালার্ম শব্দ", "language" to "ভাষা",
        "reset" to "রিসেট", "start" to "শুরু", "pause" to "বিরতি", "custom" to "কাস্টম",
        "lap" to "ল্যাপ", "done" to "সম্পন্ন", "cancel" to "বাতিল",
        "ok" to "ঠিক আছে", "select_cities" to "শহর নির্বাচন করুন", "min" to "মিনিট", "sec" to "সেকেন্ড",
        "add_city" to "শহর যোগ করুন", "system_default" to "সিস্টেম ডিফল্ট", "close" to "বন্ধ করুন"
    ),
    "tr" to mapOf(
        "universal_timer" to "Evrensel Zamanlayıcı", "timer" to "Zamanlayıcı", "stopwatch" to "Kronometre",
        "world" to "Dünya", "settings" to "Ayarlar", "theme" to "Tema",
        "dark_mode" to "Karanlık Mod", "alarm_sound" to "Alarm Sesi", "language" to "Dil",
        "reset" to "Sıfırla", "start" to "Başlat", "pause" to "Duraklat", "custom" to "Özel",
        "lap" to "Tur", "done" to "Bitti", "cancel" to "İptal",
        "ok" to "Tamam", "select_cities" to "Şehir Seç", "min" to "Dak", "sec" to "San",
        "add_city" to "Şehir Ekle", "system_default" to "Sistem Varsayılanı", "close" to "Kapat"
    ),
    "vi" to mapOf(
        "universal_timer" to "Hẹn giờ Vạn năng", "timer" to "Hẹn giờ", "stopwatch" to "Bấm giờ",
        "world" to "Thế giới", "settings" to "Cài đặt", "theme" to "Chủ đề",
        "dark_mode" to "Chế độ tối", "alarm_sound" to "Âm báo", "language" to "Ngôn ngữ",
        "reset" to "Đặt lại", "start" to "Bắt đầu", "pause" to "Tạm dừng", "custom" to "Tùy chỉnh",
        "lap" to "Vòng", "done" to "Xong", "cancel" to "Hủy",
        "ok" to "OK", "select_cities" to "Chọn thành phố", "min" to "Phút", "sec" to "Giây",
        "add_city" to "Thêm thành phố", "system_default" to "Mặc định hệ thống", "close" to "Đóng"
    ),
    "pl" to mapOf(
        "universal_timer" to "Uniwersalny Minutnik", "timer" to "Minutnik", "stopwatch" to "Stoper",
        "world" to "Świat", "settings" to "Ustawienia", "theme" to "Motyw",
        "dark_mode" to "Tryb ciemny", "alarm_sound" to "Dźwięk alarmu", "language" to "Język",
        "reset" to "Resetuj", "start" to "Start", "pause" to "Pauza", "custom" to "Własny",
        "lap" to "Okrążenie", "done" to "Gotowe", "cancel" to "Anuluj",
        "ok" to "OK", "select_cities" to "Wybierz miasta", "min" to "Min", "sec" to "Sek",
        "add_city" to "Dodaj miasto", "system_default" to "Systemowy", "close" to "Zamknij"
    ),
    "nl" to mapOf(
        "universal_timer" to "Universele Timer", "timer" to "Timer", "stopwatch" to "Stopwatch",
        "world" to "Wereld", "settings" to "Instellingen", "theme" to "Thema",
        "dark_mode" to "Donkere Modus", "alarm_sound" to "Alarmgeluid", "language" to "Taal",
        "reset" to "Resetten", "start" to "Start", "pause" to "Pauze", "custom" to "Aangepast",
        "lap" to "Ronde", "done" to "Klaar", "cancel" to "Annuleren",
        "ok" to "OK", "select_cities" to "Selecteer steden", "min" to "Min", "sec" to "Sec",
        "add_city" to "Stad toevoegen", "system_default" to "Systeemstandaard", "close" to "Sluiten"
    ),
    "th" to mapOf(
        "universal_timer" to "ตัวจับเวลาสากล", "timer" to "ตัวจับเวลา", "stopwatch" to "นาฬิกาจับเวลา",
        "world" to "โลก", "settings" to "การตั้งค่า", "theme" to "ธีม",
        "dark_mode" to "โหมดมืด", "alarm_sound" to "เสียงปลุก", "language" to "ภาษา",
        "reset" to "รีเซ็ต", "start" to "เริ่ม", "pause" to "หยุดชั่วคราว", "custom" to "กำหนดเอง",
        "lap" to "รอบ", "done" to "เสร็จสิ้น", "cancel" to "ยกเลิก",
        "ok" to "ตกลง", "select_cities" to "เลือกเมือง", "min" to "นาที", "sec" to "วินาที",
        "add_city" to "เพิ่มเมือง", "system_default" to "ค่าเริ่มต้นระบบ", "close" to "ปิด"
    ),
    "id" to mapOf(
        "universal_timer" to "Timer Universal", "timer" to "Timer", "stopwatch" to "Stopwatch",
        "world" to "Dunia", "settings" to "Pengaturan", "theme" to "Tema",
        "dark_mode" to "Mode Gelap", "alarm_sound" to "Suara Alarm", "language" to "Bahasa",
        "reset" to "Atur Ulang", "start" to "Mulai", "pause" to "Jeda", "custom" to "Kustom",
        "lap" to "Putaran", "done" to "Selesai", "cancel" to "Batal",
        "ok" to "OK", "select_cities" to "Pilih Kota", "min" to "Menit", "sec" to "Detik",
        "add_city" to "Tambah Kota", "system_default" to "Default Sistem", "close" to "Tutup"
    ),
    "el" to mapOf(
        "universal_timer" to "Παγκόσμιο Χρονόμετρο", "timer" to "Χρονόμετρο", "stopwatch" to "Χρονόμετρο αγώνων",
        "world" to "Κόσμος", "settings" to "Ρυθμίσεις", "theme" to "Θέμα",
        "dark_mode" to "Σκοτεινή λειτουργία", "alarm_sound" to "Ήχος ειδοποίησης", "language" to "Γλώσσα",
        "reset" to "Επαναφορά", "start" to "Έναρξη", "pause" to "Παύση", "custom" to "Προσαρμογή",
        "lap" to "Γύρος", "done" to "Τέλος", "cancel" to "Ακύρωση",
        "ok" to "OK", "select_cities" to "Επιλογή πόλεων", "min" to "Λεπ", "sec" to "Δευτ",
        "add_city" to "Προσθήκη πόλης", "system_default" to "Προεπιλογή συστήματος", "close" to "Κλείσιμο"
    ),
    "he" to mapOf(
        "universal_timer" to "טיימר אוניברסלי", "timer" to "טיימר", "stopwatch" to "סטופר",
        "world" to "עולם", "settings" to "הגדרות", "theme" to "ערכת נושא",
        "dark_mode" to "מצב כהה", "alarm_sound" to "צליל התראה", "language" to "שפה",
        "reset" to "איפוס", "start" to "התחל", "pause" to "השהה", "custom" to "מותאם אישית",
        "lap" to "הקפה", "done" to "בוצע", "cancel" to "ביטול",
        "ok" to "אישור", "select_cities" to "בחר ערים", "min" to "דק'", "sec" to "שנ'",
        "add_city" to "הוסף עיר", "system_default" to "ברירת מחדל של המערכת", "close" to "סגור"
    ),
    "sv" to mapOf(
        "universal_timer" to "Universell Timer", "timer" to "Timer", "stopwatch" to "Stoppur",
        "world" to "Värld", "settings" to "Inställningar", "theme" to "Tema",
        "dark_mode" to "Mörkt läge", "alarm_sound" to "Alarmsignal", "language" to "Språk",
        "reset" to "Återställ", "start" to "Starta", "pause" to "Pausa", "custom" to "Anpassad",
        "lap" to "Varv", "done" to "Klar", "cancel" to "Avbryt",
        "ok" to "OK", "select_cities" to "Välj städer", "min" to "Min", "sec" to "Sek",
        "add_city" to "Lägg till stad", "system_default" to "Systemstandard", "close" to "Stäng"
    ),
    "no" to mapOf(
        "universal_timer" to "Universell Timer", "timer" to "Timer", "stopwatch" to "Stoppeklokke",
        "world" to "Verden", "settings" to "Innstillinger", "theme" to "Tema",
        "dark_mode" to "Mørk modus", "alarm_sound" to "Alarmlyd", "language" to "Språk",
        "reset" to "Nullstill", "start" to "Start", "pause" to "Pause", "custom" to "Tilpasset",
        "lap" to "Runde", "done" to "Ferdig", "cancel" to "Avbryt",
        "ok" to "OK", "select_cities" to "Velg byer", "min" to "Min", "sec" to "Sek",
        "add_city" to "Legg til by", "system_default" to "Systemstandard", "close" to "Lukk"
    ),
    "da" to mapOf(
        "universal_timer" to "Universel Timer", "timer" to "Timer", "stopwatch" to "Stopur",
        "world" to "Verden", "settings" to "Indstillinger", "theme" to "Tema",
        "dark_mode" to "Mørk tilstand", "alarm_sound" to "Alarmlyd", "language" to "Sprog",
        "reset" to "Nulstil", "start" to "Start", "pause" to "Pause", "custom" to "Brugerdefineret",
        "lap" to "Omgang", "done" to "Færdig", "cancel" to "Annuller",
        "ok" to "OK", "select_cities" to "Vælg byer", "min" to "Min", "sec" to "Sek",
        "add_city" to "Tilføj by", "system_default" to "Systemstandard", "close" to "Luk"
    ),
    "fi" to mapOf(
        "universal_timer" to "Yleiskäyttöinen ajastin", "timer" to "Ajastin", "stopwatch" to "Sekuntikello",
        "world" to "Maailma", "settings" to "Asetukset", "theme" to "Teema",
        "dark_mode" to "Tumma tila", "alarm_sound" to "Hälytysääni", "language" to "Kieli",
        "reset" to "Nollaa", "start" to "Aloita", "pause" to "Keskeytä", "custom" to "Mukautettu",
        "lap" to "Kierros", "done" to "Valmis", "cancel" to "Peruuta",
        "ok" to "OK", "select_cities" to "Valitse kaupungit", "min" to "Min", "sec" to "Sek",
        "add_city" to "Lisää kaupunki", "system_default" to "Järjestelmän oletus", "close" to "Sulje"
    )
)

@Composable
fun stringResource(key: String, languageCode: String): String {
    val lang = if (languageCode == "system") Locale.getDefault().language else languageCode
    return Translations[lang]?.get(key) ?: Translations["en"]?.get(key) ?: key
}
