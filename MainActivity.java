package ru.novayazhizn.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import android.content.DialogInterface;
import java.util.*;

public class MainActivity extends Activity {
    LifeView view;
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(8,9,14));
        getWindow().setNavigationBarColor(Color.rgb(8,9,14));
        view = new LifeView(this);
        setContentView(view);
    }
}

class LifeView extends View {
    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    SharedPreferences sp;
    int screen = 0; // 0 home, 1 career, 2 shop, 3 hobbies, 4 love, 5 business, 6 family
    int day, money, energy, mood, health, career, cooking, music, sport, art, love, reputation;
    int house, car, phone, computer, cafe, store, studio;
    String job, partner;
    String playerName, playerGender, city;
    int age;
    float downX, downY;
    Random random = new Random();

    int bg = Color.rgb(9,10,17), card = Color.rgb(20,22,32), card2 = Color.rgb(27,29,41);
    int white = Color.rgb(246,247,250), muted = Color.rgb(170,174,187);
    int red = Color.rgb(255,82,108), red2 = Color.rgb(185,45,72), gold = Color.rgb(255,207,112);

    LifeView(Context c) {
        super(c);
        p.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(2);
        sp = c.getSharedPreferences("life", Context.MODE_PRIVATE);
        load();
        screen = sp.getBoolean("created", false) ? 0 : 7;
        setFocusable(true);
    }

    void load() {
        day=sp.getInt("day",1); money=sp.getInt("money",1200); energy=sp.getInt("energy",82);
        mood=sp.getInt("mood",78); health=sp.getInt("health",80); career=sp.getInt("career",5);
        cooking=sp.getInt("cooking",0); music=sp.getInt("music",0); sport=sp.getInt("sport",0);
        art=sp.getInt("art",0); love=sp.getInt("love",35); reputation=sp.getInt("rep",10);
        house=sp.getInt("house",0); car=sp.getInt("car",0); phone=sp.getInt("phone",0);
        computer=sp.getInt("computer",0); cafe=sp.getInt("cafe",0); store=sp.getInt("store",0); studio=sp.getInt("studio",0);
        job=sp.getString("job","Безработный"); partner=sp.getString("partner","Свободен");
        playerName=sp.getString("playerName",""); playerGender=sp.getString("playerGender","Не выбран");
        city=sp.getString("city","Москва"); age=sp.getInt("age",18);
    }

    void save() {
        sp.edit().putInt("day",day).putInt("money",money).putInt("energy",energy).putInt("mood",mood)
            .putInt("health",health).putInt("career",career).putInt("cooking",cooking).putInt("music",music)
            .putInt("sport",sport).putInt("art",art).putInt("love",love).putInt("rep",reputation)
            .putInt("house",house).putInt("car",car).putInt("phone",phone).putInt("computer",computer)
            .putInt("cafe",cafe).putInt("store",store).putInt("studio",studio).putString("job",job)
            .putString("partner",partner).putString("playerName",playerName).putString("playerGender",playerGender)
            .putString("city",city).putInt("age",age).apply();
    }

    void reset() {
        sp.edit().clear().apply(); load(); screen=0; invalidate();
    }

    protected void onDraw(Canvas c) {
        super.onDraw(c);
        drawBackground(c);
        if(screen==7) characterCreation(c);
        else if(screen==0) home(c);
        else if(screen==1) career(c);
        else if(screen==2) shop(c);
        else if(screen==3) hobbies(c);
        else if(screen==4) love(c);
        else if(screen==5) business(c);
        else family(c);
        nav(c);
    }

    void drawBackground(Canvas c) {
        LinearGradient g = new LinearGradient(0,0,0,getHeight(),Color.rgb(16,17,28),bg,Shader.TileMode.CLAMP);
        p.setShader(g); c.drawRect(0,0,getWidth(),getHeight(),p); p.setShader(null);
        p.setColor(Color.argb(35,255,82,108));
        c.drawCircle(getWidth()-40,80,150,p);
        p.setColor(Color.argb(25,255,207,112));
        c.drawCircle(40,360,170,p);
    }

    void text(Canvas c,String s,float x,float y,float size,int color,boolean bold) {
        p.setColor(color); p.setTextSize(size); p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));
        c.drawText(s,x,y,p);
    }

    void center(Canvas c,String s,float x,float y,float size,int color,boolean bold) {
        p.setTextSize(size); p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));
        text(c,s,x-p.measureText(s)/2,y,size,color,bold);
    }

    void round(Canvas c,float l,float t,float r,float b,float rad,int color) {
        p.setColor(color); c.drawRoundRect(l,t,r,b,rad,rad,p);
    }

    void outline(Canvas c,float l,float t,float r,float b,float rad,int color) {
        stroke.setColor(color); stroke.setStyle(Paint.Style.STROKE); c.drawRoundRect(l,t,r,b,rad,rad,stroke);
    }

    void title(Canvas c,String a,String b) {
        text(c,a,24,54,30,white,true);
        text(c,b,24,82,15,muted,false);
    }

    void stat(Canvas c,float x,float y,String label,String value,int color) {
        round(c,x,y,x+104,y+72,18,card2);
        text(c,label,x+13,y+24,12,muted,false);
        text(c,value,x+13,y+53,21,color,true);
    }


    void characterCreation(Canvas c) {
        title(c,"Новая жизнь","Сначала создай своего персонажа");
        round(c,20,105,getWidth()-20,690,26,card);
        text(c,"Кто ты?",40,143,24,white,true);
        text(c,"Имя",40,176,12,muted,false);
        text(c,playerName.isEmpty()?"Нажми, чтобы ввести имя":playerName,40,202,19,
             playerName.isEmpty()?muted:white,true);
        outline(c,34,155,getWidth()-34,220,16,playerName.isEmpty()?Color.rgb(75,78,92):red);

        text(c,"Возраст",40,251,12,muted,false);
        text(c,age+" лет",40,278,19,white,true);
        buttonSmall(c,40,292,125,340,"18",age==18);
        buttonSmall(c,134,292,219,340,"25",age==25);
        buttonSmall(c,228,292,313,340,"35",age==35);
        buttonSmall(c,322,292,407,340,"45",age==45);

        text(c,"Пол",40,373,12,muted,false);
        buttonSmall(c,40,390,175,440,"Мужчина",playerGender.equals("Мужчина"));
        buttonSmall(c,190,390,325,440,"Женщина",playerGender.equals("Женщина"));

        text(c,"Город старта",40,475,12,muted,false);
        cityButton(c,40,492,210,548,"Москва","Столица • карьера");
        cityButton(c,222,492,392,548,"Санкт-Петербург","Культура • творчество");
        cityButton(c,40,558,210,614,"Казань","Технологии • бизнес");
        cityButton(c,222,558,392,614,"Сочи","Туризм • отдых");
        cityButton(c,40,624,210,680,"Новосибирск","Наука • зарплаты");

        boolean ready=!playerName.isEmpty() && !playerGender.equals("Не выбран") && city!=null && !city.isEmpty();
        if(ready) {
            // The bottom action is intentionally compact so it remains usable on small screens.
            button(c,20,704,getWidth()-20,764,"✨ Начать новую жизнь",red);
        } else {
            text(c,"Заполни имя, возраст, пол и выбери город",40,716,12,muted,false);
        }
    }

    void buttonSmall(Canvas c,float l,float t,float r,float b,String label,boolean active) {
        round(c,l,t,r,b,15,active?Color.rgb(65,24,34):card2);
        outline(c,l,t,r,b,15,active?red:Color.rgb(60,63,76));
        center(c,label,(l+r)/2,t+31,14,active?white:muted,true);
    }

    void cityButton(Canvas c,float l,float t,float r,float b,String name,String bonus) {
        boolean active=name.equals(city);
        round(c,l,t,r,b,15,active?Color.rgb(65,24,34):card2);
        outline(c,l,t,r,b,15,active?red:Color.rgb(60,63,76));
        text(c,name,l+12,t+23,14,white,true);
        text(c,bonus,l+12,t+43,10,muted,false);
    }

    void askName() {
        final EditText input = new EditText(getContext());
        input.setSingleLine(true);
        input.setHint("Например, Алексей");
        input.setText(playerName);
        input.setSelectAllOnFocus(true);
        new AlertDialog.Builder(getContext())
            .setTitle("Как тебя зовут?")
            .setView(input)
            .setNegativeButton("Отмена",null)
            .setPositiveButton("Готово",(d,w)->{
                String n=input.getText().toString().trim();
                if(!n.isEmpty()) { playerName=n; save(); invalidate(); }
            }).show();
    }

    void home(Canvas c) {
        title(c,"Новая жизнь","Ты решаешь, каким будет следующий день");
        round(c,20,104,getWidth()-20,246,24,card);
        text(c,"День "+day+"  •  "+city,38,137,15,gold,true);
        text(c,playerName+" • "+age+" лет",38,158,13,muted,false);
        text(c,job,38,184,22,white,true);
        text(c,"💰 "+money+" ₽",38,215,16,white,false);
        text(c,"Энергия "+energy+"%   Настроение "+mood+"%",38,239,13,muted,false);
        stat(c,20,266,"Здоровье",health+"%",Color.rgb(93,219,142));
        stat(c,132,266,"Карьера",career+"%",gold);
        stat(c,244,266,"Любовь",love+"%",red);
        round(c,20,356,getWidth()-20,420,20,Color.rgb(50,22,30));
        text(c,"☀  Сегодняшний план",38,382,16,white,true);
        text(c,"Выбери действие в меню ниже",38,405,13,muted,false);

        button(c,20,444,getWidth()-20,506,"▶  Начать новый день",red);
        button(c,20,518,getWidth()-20,580,"🎲  Случайное событие",Color.rgb(75,60,85));

        text(c,"Быстрый статус",24,620,21,white,true);
        text(c,"Дом: "+(house>0?"есть":"нет")+"     Машина: "+(car>0?"есть":"нет"),24,648,14,muted,false);
        text(c,"Партнёр: "+partner,24,674,14,muted,false);
    }

    void career(Canvas c) {
        title(c,"💼 Карьера","Работа, навыки и путь наверх");
        cardItem(c,20,105,"Курьер","80–180 ₽ / день","Доступно всегда", job.equals("Курьер"));
        cardItem(c,20,181,"Бариста","120–260 ₽ / день","Нужно настроение 45+", job.equals("Бариста"));
        cardItem(c,20,257,"Программист","300–650 ₽ / день","Нужен компьютер", job.equals("Программист"));
        cardItem(c,20,333,"Шеф-повар","420–850 ₽ / день","Кулинария 20+", job.equals("Шеф-повар"));
        cardItem(c,20,409,"Менеджер","650–1200 ₽ / день","Карьера 55+", job.equals("Менеджер"));
        text(c,"Нажми на вакансию, чтобы устроиться",24,488,13,muted,false);
        round(c,20,510,getWidth()-20,590,20,card);
        text(c,"Текущая карьера",38,540,14,muted,false);
        text(c,job+"  •  уровень "+Math.max(1,career/10),38,567,20,white,true);
    }

    void cardItem(Canvas c,float x,float y,String name,String pay,String req,boolean active) {
        round(c,x,y,getWidth()-20,y+64,18,active?Color.rgb(62,24,34):card);
        outline(c,x,y,getWidth()-20,y+64,18,active?red:Color.rgb(52,55,68));
        text(c,name,x+16,y+25,18,white,true);
        text(c,pay,x+16,y+48,12,muted,false);
        text(c,req,getWidth()-180,y+38,11,active?red:muted,false);
    }

    void shop(Canvas c) {
        title(c,"🛍 Магазин","Покупай вещи, которые меняют твою жизнь");
        item(c,20,105,"📱 Смартфон",700,"Настроение +5",phone);
        item(c,20,177,"💻 Ноутбук",1400,"Навык +5",computer);
        item(c,20,249,"🚗 Машина",6500,"Комфорт +12",car);
        item(c,20,321,"🏠 Квартира",12000,"Настроение +18",house);
        text(c,"Баланс: "+money+" ₽",24,420,20,gold,true);
        text(c,"Совет: сначала вложись в навык или работу.",24,448,13,muted,false);
    }

    void item(Canvas c,float x,float y,String name,int price,String bonus,int owned) {
        round(c,x,y,getWidth()-20,y+60,18,card);
        text(c,name,x+15,y+25,17,white,true);
        text(c,bonus,x+15,y+46,11,muted,false);
        text(c,owned>0?"Куплено":"−"+price+" ₽",getWidth()-105,y+34,12,owned>0?gold:red,true);
    }

    void hobbies(Canvas c) {
        title(c,"🎨 Хобби","Развивай навыки — они могут стать карьерой");
        skill(c,20,105,"🎸 Музыка",music,"music");
        skill(c,20,178,"🍳 Кулинария",cooking,"cooking");
        skill(c,20,251,"⚽ Спорт",sport,"sport");
        skill(c,20,324,"🎨 Рисование",art,"art");
        round(c,20,415,getWidth()-20,493,20,card);
        text(c,"Каждая тренировка тратит 12 энергии",38,445,14,muted,false);
        text(c,"и повышает настроение.",38,468,14,muted,false);
    }

    void skill(Canvas c,float x,float y,String name,int val,String key) {
        round(c,x,y,getWidth()-20,y+60,18,card);
        text(c,name,x+15,y+25,17,white,true);
        text(c,"Уровень "+val,x+15,y+47,12,muted,false);
        round(c,getWidth()-118,y+20,getWidth()-38,y+31,6,Color.rgb(46,48,60));
        round(c,getWidth()-118,y+20,getWidth()-118+80*Math.min(1,val/100f),y+31,6,red);
    }

    void love(Canvas c) {
        title(c,"❤️ Отношения","Связи меняют настроение и открывают события");
        round(c,20,105,getWidth()-20,205,22,card);
        text(c,partner,38,138,23,white,true);
        text(c,"Уровень отношений: "+love+"%",38,168,14,muted,false);
        text(c,"Любовь влияет на события и семейную жизнь.",38,191,12,muted,false);
        button(c,20,226,getWidth()-20,288,"❤️ Познакомиться",red);
        button(c,20,300,getWidth()-20,362,"💬 Позвонить партнёру",Color.rgb(68,46,70));
        button(c,20,374,getWidth()-20,436,"🌹 Свидание",Color.rgb(92,42,52));
        if(!partner.equals("Свободен")) text(c,"Следующая цель: совместный дом и семья.",24,480,14,muted,false);
    }

    void business(Canvas c) {
        title(c,"🏢 Бизнес","Создай источник дохода, который работает на тебя");
        businessItem(c,20,105,"☕ Кофейня",cafe,5000,"+250 ₽ / день");
        businessItem(c,20,180,"👕 Магазин",store,9000,"+430 ₽ / день");
        businessItem(c,20,255,"📷 Фотостудия",studio,14000,"+700 ₽ / день");
        text(c,"Бизнес приносит доход в конце каждого дня.",24,350,13,muted,false);
        text(c,"Баланс: "+money+" ₽",24,382,20,gold,true);
    }

    void businessItem(Canvas c,float x,float y,String name,int owned,int price,String income) {
        round(c,x,y,getWidth()-20,y+62,18,owned>0?Color.rgb(48,35,28):card);
        text(c,name,x+15,y+25,17,white,true);
        text(c,owned>0?income:"Открыть за "+price+" ₽",x+15,y+47,12,owned>0?gold:muted,false);
        if(owned>0) text(c,"✓",getWidth()-54,y+38,20,gold,true);
    }

    void family(Canvas c) {
        title(c,"👨‍👩‍👧 Семья","Дом, отношения и большие жизненные решения");
        round(c,20,105,getWidth()-20,190,22,card);
        text(c,"Семейный статус",38,135,13,muted,false);
        text(c,partner.equals("Свободен")?"Ты пока один":"Вы вместе",38,164,22,white,true);
        button(c,20,210,getWidth()-20,272,partner.equals("Свободен")?"💍 Сделать предложение":"🏠 Планировать семью",red);
        button(c,20,284,getWidth()-20,346,"🧸 Подумать о ребёнке",Color.rgb(67,45,59));
        round(c,20,378,getWidth()-20,460,20,card);
        text(c,"Семейный дом",38,410,14,muted,false);
        text(c,house>0?"Есть место для большой семьи":"Сначала купи квартиру",38,438,17,white,true);
    }

    void nav(Canvas c) {
        float top=getHeight()-92;
        p.setColor(Color.argb(245,8,9,14)); c.drawRect(0,top,getWidth(),getHeight(),p);
        String[] names={"Главная","Работа","Магазин","Хобби","Любовь","Ещё"};
        int[] ids={0,1,2,3,4,5};
        float w=getWidth()/6f;
        for(int i=0;i<6;i++){
            float cx=w*i+w/2;
            if((screen==5 && i==5) || screen==6 && i==5) {
                round(c,cx-w/2+7,top+9,cx+w/2-7,top+66,18,Color.rgb(55,24,32));
            } else if(screen==i) {
                round(c,cx-w/2+7,top+9,cx+w/2-7,top+66,18,Color.rgb(60,22,32));
            }
            String icon = new String[]{"⌂","▣","□","✦","♡","☰"}[i];
            center(c,icon,cx,top+34,23,(screen==i|| (i==5&&(screen==6)))?red:white,true);
            center(c,names[i],cx,top+55,10,(screen==i|| (i==5&&(screen==6)))?white:muted,false);
        }
    }

    void button(Canvas c,float l,float t,float r,float b,String label,int color) {
        round(c,l,t,r,b,18,color);
        center(c,label,(l+r)/2,t+39,16,white,true);
    }

    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}
        if(e.getAction()==MotionEvent.ACTION_UP){
            float x=e.getX(), y=e.getY();
            if(Math.abs(x-downX)>80 && Math.abs(y-downY)<80 && screen==0){ return true; }
            if(screen==7) { handleCharacterTap(x,y); return true; }
            if(y>getHeight()-105){ 
                int i=(int)(x/(getWidth()/6f));
                if(i<5) screen=i; else screen=5;
                invalidate(); return true;
            }
            handleTap(x,y);
            return true;
        }
        return true;
    }


    void handleCharacterTap(float x,float y) {
        if(y>=155 && y<=225) { askName(); return; }
        if(y>=292 && y<=350) {
            if(x<130) age=18; else if(x<223) age=25; else if(x<317) age=35; else age=45;
            save(); invalidate(); return;
        }
        if(y>=390 && y<=450) {
            playerGender = x<185 ? "Мужчина" : "Женщина";
            save(); invalidate(); return;
        }
        if(y>=492 && y<=552) {
            city = x<216 ? "Москва" : "Санкт-Петербург";
            save(); invalidate(); return;
        }
        if(y>=558 && y<=618) {
            city = x<216 ? "Казань" : "Сочи";
            save(); invalidate(); return;
        }
        if(y>=624 && y<=690) {
            city = "Новосибирск";
            save(); invalidate(); return;
        }
        if(y>=700 && y<=790 && !playerName.isEmpty() && !playerGender.equals("Не выбран")) {
            startLife();
        }
    }

    void handleTap(float x,float y) {
        if(screen==0){
            if(y>438 && y<510) nextDay();
            else if(y>512 && y<590) event();
        } else if(screen==1) {
            if(y>105&&y<169) chooseJob("Курьер",120);
            else if(y>181&&y<245) chooseJob("Бариста",180);
            else if(y>257&&y<321) chooseJob("Программист",450);
            else if(y>333&&y<397 && cooking>=20) chooseJob("Шеф-повар",620);
            else if(y>409&&y<473 && career>=55) chooseJob("Менеджер",900);
        } else if(screen==2) {
            if(y>105&&y<165) buy("phone",700);
            else if(y>177&&y<237) buy("computer",1400);
            else if(y>249&&y<309) buy("car",6500);
            else if(y>321&&y<381) buy("house",12000);
        } else if(screen==3) {
            if(y>105&&y<165) train("music");
            else if(y>178&&y<238) train("cooking");
            else if(y>251&&y<311) train("sport");
            else if(y>324&&y<384) train("art");
        } else if(screen==4) {
            if(y>226&&y<288) meet();
            else if(y>300&&y<362) talk();
            else if(y>374&&y<436) date();
        } else if(screen==5) {
            if(y>105&&y<167) buyBusiness("cafe",5000);
            else if(y>180&&y<242) buyBusiness("store",9000);
            else if(y>255&&y<317) buyBusiness("studio",14000);
        } else if(screen==6) {
            if(y>210&&y<272) familyAction();
            else if(y>284&&y<346) childAction();
        }
    }


    void startLife() {
        if(playerName.isEmpty() || playerGender.equals("Не выбран") || city.isEmpty()) return;
        // Small city-specific starting bonuses.
        if(city.equals("Москва")) { money=1600; career=12; reputation=14; }
        else if(city.equals("Санкт-Петербург")) { money=1350; art=8; music=8; mood=84; }
        else if(city.equals("Казань")) { money=1500; computer=1; career=10; }
        else if(city.equals("Сочи")) { money=1400; mood=90; health=86; }
        else if(city.equals("Новосибирск")) { money=1450; health=84; career=9; }
        sp.edit().putBoolean("created",true).apply();
        save();
        screen=0;
        invalidate();
    }

    void nextDay() {
        day++; energy=Math.min(100,energy+35); mood=Math.min(100,mood+5);
        if(!job.equals("Безработный")) {
            int pay=120;
            if(job.equals("Бариста")) pay=180; if(job.equals("Программист")) pay=450;
            if(job.equals("Шеф-повар")) pay=620; if(job.equals("Менеджер")) pay=900;
            money+=pay; career=Math.min(100,career+2);
        }
        if(cafe>0) money+=250; if(store>0) money+=430; if(studio>0) money+=700;
        save(); invalidate();
    }

    void event() {
        int n=random.nextInt(4);
        if(n==0){money+=250; mood+=8;}
        else if(n==1){money=Math.max(0,money-120); health-=4;}
        else if(n==2){career=Math.min(100,career+5); reputation+=3;}
        else {love=Math.min(100,love+7); mood=Math.min(100,mood+10);}
        energy=Math.max(0,energy-6); save(); invalidate();
    }

    void chooseJob(String j,int pay){ job=j; mood=Math.min(100,mood+5); career=Math.min(100,career+4); save(); invalidate(); }
    void buy(String what,int price){
        if(money<price)return;
        money-=price;
        if(what.equals("phone"))phone=1;
        if(what.equals("computer"))computer=1;
        if(what.equals("car"))car=1;
        if(what.equals("house"))house=1;
        save(); invalidate();
    }
    void train(String what){
        if(energy<12)return;
        energy-=12; mood=Math.min(100,mood+5);
        if(what.equals("music"))music=Math.min(100,music+7);
        if(what.equals("cooking"))cooking=Math.min(100,cooking+7);
        if(what.equals("sport")){sport=Math.min(100,sport+7);health=Math.min(100,health+4);}
        if(what.equals("art"))art=Math.min(100,art+7);
        save(); invalidate();
    }
    void meet(){ love=Math.min(100,love+8); mood=Math.min(100,mood+6); if(love>45) partner="Новый человек"; save(); invalidate(); }
    void talk(){ if(partner.equals("Свободен")) meet(); else {love=Math.min(100,love+6); mood=Math.min(100,mood+5);save();invalidate();}}
    void date(){ if(partner.equals("Свободен")) meet(); else {money=Math.max(0,money-80);love=Math.min(100,love+14);mood=Math.min(100,mood+15);energy=Math.max(0,energy-10);save();invalidate();}}
    void buyBusiness(String what,int price){
        if(money<price)return; money-=price;
        if(what.equals("cafe"))cafe=1; if(what.equals("store"))store=1; if(what.equals("studio"))studio=1;
        reputation+=4; save(); invalidate();
    }
    void familyAction(){ if(!partner.equals("Свободен") && house>0){love=Math.min(100,love+10);mood=Math.min(100,mood+8);partner="Семья";} else if(partner.equals("Свободен")) meet(); save();invalidate(); }
    void childAction(){ if(partner.equals("Семья") && house>0){mood=Math.max(0,mood-4);reputation+=2;} save();invalidate(); }
}
