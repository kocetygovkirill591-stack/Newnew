const KEY="novaya-zhizn-save-v1";
const cities=[
 {id:"moscow",name:"Москва",emoji:"🏙️",cost:18000,desc:"Большой город, высокая зарплата и высокий ритм.",jobs:["Программист","Менеджер","Курьер"]},
 {id:"spb",name:"Санкт-Петербург",emoji:"🌉",cost:14000,desc:"Творческая атмосфера, культура и умеренный ритм.",jobs:["Дизайнер","Бариста","Разработчик"]},
 {id:"sochi",name:"Сочи",emoji:"🌴",cost:16000,desc:"Море, туризм и сезонный бизнес.",jobs:["Администратор","Фитнес-тренер","Бариста"]},
 {id:"novosib",name:"Новосибирск",emoji:"🏢",cost:10000,desc:"Доступное жильё и сильный рынок технологий.",jobs:["Инженер","Программист","Водитель"]},
 {id:"kazan",name:"Казань",emoji:"🕌",cost:12000,desc:"Быстро растущий город с сильным малым бизнесом.",jobs:["Менеджер","Инженер","Ресторатор"]}
];
const jobs=[
 {name:"Курьер",pay:2200,energy:14,skill:"Выносливость"},
 {name:"Бариста",pay:2600,energy:10,skill:"Общение"},
 {name:"Менеджер",pay:4300,energy:9,skill:"Общение"},
 {name:"Программист",pay:6200,energy:8,skill:"Интеллект"},
 {name:"Дизайнер",pay:5200,energy:8,skill:"Творчество"},
 {name:"Инженер",pay:5600,energy:9,skill:"Интеллект"},
 {name:"Администратор",pay:3800,energy:10,skill:"Общение"},
 {name:"Фитнес-тренер",pay:4100,energy:14,skill:"Выносливость"},
 {name:"Водитель",pay:3500,energy:13,skill:"Выносливость"},
 {name:"Ресторатор",pay:6800,energy:11,skill:"Общение"}
];

const careerLevels=[
 {level:1,title:"Стажёр",mult:1.00,need:0},
 {level:2,title:"Младший специалист",mult:1.20,need:70},
 {level:3,title:"Специалист",mult:1.45,need:150},
 {level:4,title:"Старший специалист",mult:1.75,need:260},
 {level:5,title:"Руководитель",mult:2.15,need:400}
];

const businesses=[
 {name:"Кофейня",cost:80000,profit:5000,req:"Общение"},
 {name:"Магазин одежды",cost:120000,profit:7500,req:"Общение"},
 {name:"IT-студия",cost:180000,profit:11000,req:"Интеллект"},
 {name:"Фитнес-клуб",cost:220000,profit:13000,req:"Выносливость"},
 {name:"Ресторан",cost:300000,profit:18000,req:"Общение"}
];
const people=[
 {name:"Алина",emoji:"👩🏻",love:8,job:"Дизайнер",text:"Любит прогулки и уютные места."},
 {name:"Катя",emoji:"👩🏼",love:7,job:"Врач",text:"Спокойная и заботливая."},
 {name:"Даша",emoji:"👩🏻‍🦰",love:9,job:"Маркетолог",text:"Амбициозная и любит путешествия."},
 {name:"Марина",emoji:"👩🏽",love:6,job:"Фотограф",text:"Творческая душа."}
];

let state=load()||{
 created:false,name:"",age:24,gender:"Мужской",city:"moscow",money:15000,energy:100,
 health:100,happy:60,intellect:50,communication:50,endurance:50,creativity:50,
 job:null,jobLevel:1,jobXp:0,business:null,partner:null,children:0,day:1,screen:"home",log:["Добро пожаловать в новую жизнь!"]
};
function load(){try{return JSON.parse(localStorage.getItem(KEY))}catch(e){return null}}
function save(){localStorage.setItem(KEY,JSON.stringify(state))}
function city(){return cities.find(c=>c.id===state.city)}
function job(){return jobs.find(j=>j.name===state.job)}
function biz(){return businesses.find(b=>b.name===state.business)}
function money(n){return new Intl.NumberFormat("ru-RU").format(Math.round(n))+" ₽"}
function addLog(t){state.log.unshift(`День ${state.day}: ${t}`);state.log=state.log.slice(0,12)}
function toast(t){const x=document.createElement("div");x.className="toast";x.textContent=t;document.body.append(x);setTimeout(()=>x.remove(),2200)}
function render(){
 document.querySelectorAll(".bottom-nav button").forEach(b=>b.classList.toggle("active",b.dataset.screen===state.screen));
 document.getElementById("cityLine").textContent=state.created?`${city().emoji} ${city().name} • день ${state.day}`:"Создай персонажа и начни историю";
 const s=document.getElementById("screen");
 if(!state.created){renderCreate(s);return}
 ({home:renderHome,work:renderWork,city:renderCity,relations:renderRelations,business:renderBusiness}[state.screen]||renderHome)(s);
 save();
}
function renderCreate(s){
 s.innerHTML=`<section class="hero"><h1>Новая жизнь</h1><p>Создай героя, выбери город и начни собственную историю: работа, отношения, семья, бизнес и переезды.</p><div class="chips"><span class="chip">5 городов</span><span class="chip">Работа</span><span class="chip">Семья</span><span class="chip">Бизнес</span></div></section>
 <h2 class="section-title">Создание персонажа</h2>
 <div class="card form">
 <label>Имя<input id="pname" placeholder="Например, Алексей"></label>
 <label>Возраст<input id="page" type="number" min="18" max="70" value="24"></label>
 <label>Пол</label><div class="choice-grid"><button class="choice selected" data-g="Мужской">👨 Мужской</button><button class="choice" data-g="Женский">👩 Женский</button></div>
 <label>Стартовый город</label><div class="choice-grid">${cities.map(c=>`<button class="choice ${c.id===state.city?"selected":""}" data-city="${c.id}">${c.emoji} ${c.name}<small>${c.desc}</small></button>`).join("")}</div>
 <button class="cta" id="create">Начать новую жизнь</button></div>`;
 document.querySelectorAll("[data-g]").forEach(b=>b.onclick=()=>{document.querySelectorAll("[data-g]").forEach(x=>x.classList.remove("selected"));b.classList.add("selected");state.gender=b.dataset.g});
 document.querySelectorAll("[data-city]").forEach(b=>b.onclick=()=>{document.querySelectorAll("[data-city]").forEach(x=>x.classList.remove("selected"));b.classList.add("selected");state.city=b.dataset.city});
 document.getElementById("create").onclick=()=>{state.name=document.getElementById("pname").value.trim()||"Алексей";state.age=+document.getElementById("page").value||24;state.created=true;addLog(`Ты приехал в ${city().name}.`);render()};
}
function renderHome(s){
 const j=job(),b=biz();
 s.innerHTML=`<section class="hero"><div class="profile"><div class="avatar">${state.gender==="Мужской"?"👨":"👩"}</div><div><h1>${state.name}</h1><p>${state.age} лет • ${city().emoji} ${city().name}</p></div></div><div class="grid"><div class="card"><div class="muted">День</div><b>${state.day}</b></div><div class="card"><div class="muted">Деньги</div><b class="money">${money(state.money)}</b></div></div></section>
 <h2 class="section-title">Твои показатели</h2><div class="grid">${stat("⚡ Энергия",state.energy)}${stat("❤️ Здоровье",state.health)}${stat("😊 Счастье",state.happy)}${stat("🧠 Интеллект",state.intellect)}${stat("💬 Общение",state.communication)}${stat("🎨 Творчество",state.creativity)}</div>
 <h2 class="section-title">Развитие навыков</h2>
 <div class="grid">
   <div class="card"><h3>🧠 Интеллект ${state.intellect}</h3><button class="btn blue" onclick="trainSkill('intellect')">Учиться — 500 ₽</button></div>
   <div class="card"><h3>💬 Общение ${state.communication}</h3><button class="btn blue" onclick="trainSkill('communication')">Тренировать — 500 ₽</button></div>
   <div class="card"><h3>💪 Выносливость ${state.endurance}</h3><button class="btn blue" onclick="trainSkill('endurance')">Тренироваться — 500 ₽</button></div>
   <div class="card"><h3>🎨 Творчество ${state.creativity}</h3><button class="btn blue" onclick="trainSkill('creativity')">Практика — 500 ₽</button></div>
 </div>
 <h2 class="section-title">Сейчас</h2><div class="list">
 <div class="item"><div class="left"><strong>💼 Работа</strong><small>${j?j.name+" • "+money(j.pay)+" за смену":"Работы пока нет"}</small></div><button class="btn green" onclick="state.screen='work';render()">Открыть</button></div>
 <div class="item"><div class="left"><strong>❤️ Отношения</strong><small>${state.partner?`В отношениях с ${state.partner.name}${state.children?" • детей: "+state.children:""}`:"Пока свободен"}</small></div><button class="btn" onclick="state.screen='relations';render()">Открыть</button></div>
 <div class="item"><div class="left"><strong>🏢 Бизнес</strong><small>${b?b.name+" • "+money(b.profit)+"/день": "Своего бизнеса нет"}</small></div><button class="btn" onclick="state.screen='business';render()">Открыть</button></div>
 </div>
 <h2 class="section-title">Последние события</h2><div class="list">${state.log.slice(0,5).map(x=>`<div class="item"><div class="left"><small>${x}</small></div></div>`).join("")}</div>`;
}
function stat(n,v){return `<div class="card"><div class="statrow"><span>${n}</span><b>${Math.round(v)}</b></div><div class="bar"><div class="fill" style="width:${Math.max(0,Math.min(100,v))}%"></div></div></div>`}
function trainSkill(key){
 if(state.money<500)return toast("Нужно 500 ₽");
 state.money-=500;
 state[key]=Math.min(100,state[key]+5);
 state.energy=Math.max(0,state.energy-4);
 state.day++;
 const names={intellect:"Интеллект",communication:"Общение",endurance:"Выносливость",creativity:"Творчество"};
 addLog(`Ты прокачал навык «${names[key]}» до ${state[key]}.`);
 toast(`Навык «${names[key]}» повышен`);
 render()
}
function renderWork(s){
 const cl=careerLevel(), next=nextCareerLevel(), j=job();
 const progress=next?Math.min(100,Math.round((state.jobXp-next.need+careerLevels[cl.level-1].need)/(next.need-careerLevels[cl.level-1].need)*100)):100;
 const available=jobs.map(jj=>`<div class="item"><div class="left"><strong>${jj.name}</strong><small>${jj.skill} • расход энергии ${jj.energy} • база ${money(jj.pay)}/смена</small></div><div><div class="money">${state.job===jj.name?money(careerPay()):money(jj.pay)}</div><button class="btn ${state.job===jj.name?"green":""}" onclick="takeJob('${jj.name}')">${state.job===jj.name?"Твоя работа":"Выбрать"}</button></div></div>`).join("");
 s.innerHTML=`<h1>Карьера</h1><p class="muted">Развивай нужный навык, набирай опыт и получай повышение. Каждый новый уровень увеличивает зарплату.</p>
 <div class="card">
   <div class="statrow"><span>Текущая работа</span><b>${state.job||"Нет"}</b></div>
   <div class="statrow"><span>Должность</span><b class="good">${state.job?cl.title:"—"}</b></div>
   <div class="statrow"><span>Зарплата за смену</span><b class="money">${state.job?money(careerPay()):"—"}</b></div>
   <div class="statrow"><span>Опыт карьеры</span><b>${state.jobXp}${next?` / ${next.need}`:" • максимум"}</b></div>
   <div class="bar"><div class="fill" style="width:${progress}%"></div></div>
   ${next?`<p class="muted">До повышения: ещё ${Math.max(0,next.need-state.jobXp)} опыта → <b>${next.title}</b> (+${Math.round((next.mult-1)*100)}% к базовой зарплате)</p>`:`<p class="good">Максимальный уровень карьеры достигнут 👑</p>`}
   <button class="cta" onclick="workDay()">Работать сегодня</button>
 </div>
 <h2 class="section-title">Как получить повышение</h2>
 <div class="grid">
   <div class="card"><h3>📈 Работай</h3><p class="muted">Каждая смена даёт опыт карьеры.</p></div>
   <div class="card"><h3>🧠 Качай навык</h3><p class="muted">Навык, связанный с профессией, увеличивает опыт за смену.</p></div>
   <div class="card"><h3>💰 Расти в зарплате</h3><p class="muted">После повышения множитель зарплаты становится выше.</p></div>
 </div>
 <h2 class="section-title">Вакансии</h2><div class="list">${available}</div>`;
}
function takeJob(n){
 if(state.job===n)return toast("Ты уже работаешь здесь");
 state.job=n;state.jobLevel=1;state.jobXp=0;
 addLog(`Ты устроился на новую работу: ${n}. Карьера начинается со стажёра.`);
 toast("Новая работа выбрана");
 render()
}
function careerLevel(){return careerLevels.findLast(x=>state.jobXp>=x.need)||careerLevels[0]}
function nextCareerLevel(){return careerLevels.find(x=>x.level===careerLevel().level+1)||null}
function careerPay(){const j=job();return j?Math.round(j.pay*careerLevel().mult):0}
function skillValueForJob(j){
 const map={Интеллект:"intellect",Общение:"communication",Выносливость:"endurance",Творчество:"creativity"};
 return state[map[j.skill]]||0;
}
function workDay(){
 if(!state.job)return toast("Сначала выбери работу");
 const j=job();if(state.energy<j.energy)return toast("Не хватает энергии");
 const skill=skillValueForJob(j);
 const xpGain=8+Math.floor(skill/20);
 state.energy-=j.energy;
 const pay=careerPay();
 state.money+=pay;
 state.jobXp+=xpGain;
 state.happy=Math.max(0,state.happy-2);
 state.day++;
 const before=careerLevel().level;
 addLog(`Смена завершена: +${money(pay)}, опыт карьеры +${xpGain}.`);
 const after=careerLevel().level;
 if(after>before){
   addLog(`Повышение! Ты стал: ${careerLevel().title}. Зарплата выросла.`);
   toast(`🎉 Повышение: ${careerLevel().title}!`);
 }else toast(`Зарплата +${money(pay)}`);
 render()
}
function renderCity(s){
 s.innerHTML=`<h1>Города</h1><p class="muted">Можно переезжать и начинать новую главу. Переезд стоит денег.</p><div class="grid">${cities.map(c=>`<div class="card city-card"><div class="skyline">${c.emoji}</div><h3>${c.name}</h3><p class="muted">${c.desc}</p><div class="price">Переезд: ${money(c.cost)}</div><button class="btn ${c.id===state.city?"green":""}" onclick="moveCity('${c.id}')">${c.id===state.city?"Ты здесь":"Переехать"}</button></div>`).join("")}</div>`;
}
function moveCity(id){if(id===state.city)return;if(state.money<cities.find(c=>c.id===id).cost)return toast("Не хватает денег");state.money-=cities.find(c=>c.id===id).cost;state.city=id;state.day++;addLog(`Переезд в ${city().name}.`);toast(`Добро пожаловать в ${city().name}`);render()}
function renderRelations(s){
 if(state.partner){
  const p=state.partner;
  s.innerHTML=`<section class="hero"><div class="profile"><div class="avatar">${p.emoji}</div><div><h1>${p.name}</h1><p>${p.job} • ${p.text}</p></div></div><div class="grid"><div class="card"><div class="muted">Любовь</div><b>${Math.min(100,p.love*10)}%</b></div><div class="card"><div class="muted">Дети</div><b>${state.children}</b></div></div></section>
  <h2 class="section-title">Семья</h2><div class="list"><div class="item"><div class="left"><strong>💑 Отношения</strong><small>Вы вместе. Поддерживайте счастье и доход.</small></div><button class="btn green" onclick="date()">Свидание</button></div><div class="item"><div class="left"><strong>👶 Ребёнок</strong><small>Создать семью — большая ответственность.</small></div><button class="btn" onclick="child()">Завести ребёнка</button></div><div class="item"><div class="left"><strong>🏠 Семейный день</strong><small>Отдохнуть вместе и восстановить силы.</small></div><button class="btn blue" onclick="familyDay()">Провести день</button></div></div>`;
 }else{
 s.innerHTML=`<h1>Отношения</h1><p class="muted">Знакомься, строй отношения и решай, какой будет твоя семья.</p><div class="list">${people.map((p,i)=>`<div class="item"><div class="left"><strong>${p.emoji} ${p.name}</strong><small>${p.job} • ${p.text}</small></div><button class="btn" onclick="meet(${i})">Познакомиться</button></div>`).join("")}</div>`;
 }
}
function meet(i){state.partner={...people[i]};state.happy=Math.min(100,state.happy+12);addLog(`Ты познакомился с ${state.partner.name}. Отношения начались!`);toast("Новая любовь ❤️");render()}
function date(){if(state.money<1000)return toast("Нужно 1 000 ₽ на свидание");state.money-=1000;state.partner.love=Math.min(10,state.partner.love+1);state.happy=Math.min(100,state.happy+15);state.day++;addLog(`Свидание с ${state.partner.name}.`);toast("Отличный вечер ❤️");render()}
function child(){if(state.partner.love<8)return toast("Сначала укрепите отношения");if(state.money<15000)return toast("Нужно 15 000 ₽");state.money-=15000;state.children++;state.happy=Math.min(100,state.happy+8);state.day++;addLog(`В семье появился ребёнок. Теперь детей: ${state.children}.`);toast("Поздравляем с пополнением! 👶");render()}
function familyDay(){state.energy=Math.min(100,state.energy+25);state.happy=Math.min(100,state.happy+12);state.day++;addLog("Семейный день прошёл тепло и спокойно.");toast("Семья провела день вместе");render()}
function renderBusiness(s){
 const owned=biz();
 if(owned)s.innerHTML=`<section class="hero"><h1>${owned.name}</h1><p>Твой бизнес приносит стабильный доход каждый игровой день.</p><div class="grid"><div class="card"><div class="muted">Стоимость</div><b>${money(owned.cost)}</b></div><div class="card"><div class="muted">Доход/день</div><b class="money">${money(owned.profit)}</b></div></div><button class="cta" onclick="businessDay()">Заработать за день</button></section><h2 class="section-title">Управление</h2><div class="list"><div class="item"><div class="left"><strong>📈 Реклама</strong><small>Повышает прибыль бизнеса.</small></div><button class="btn blue" onclick="advertise()">Рекламировать — 10 000 ₽</button></div><div class="item"><div class="left"><strong>🚪 Закрыть бизнес</strong><small>Продать бизнес за 60% стоимости.</small></div><button class="btn red" onclick="sellBusiness()">Продать</button></div></div>`;
 else s.innerHTML=`<h1>Бизнес</h1><p class="muted">Накопи капитал и стань владельцем собственного дела.</p><div class="list">${businesses.map(b=>`<div class="item"><div class="left"><strong>🏢 ${b.name}</strong><small>Нужно: ${b.req} • прибыль ${money(b.profit)}/день</small></div><div><div class="price">${money(b.cost)}</div><button class="btn green" onclick="buyBusiness('${b.name}')">Открыть</button></div></div>`).join("")}</div>`;
}
function buyBusiness(n){const b=businesses.find(x=>x.name===n);if(state.business)return toast("У тебя уже есть бизнес");if(state.money<b.cost)return toast("Не хватает капитала");state.money-=b.cost;state.business=n;addLog(`Ты открыл бизнес: ${n}.`);toast("Бизнес открыт 🏢");render()}
function businessDay(){const b=biz();state.money+=b.profit;state.day++;addLog(`${b.name} принёс ${money(b.profit)}.`);toast("Прибыль получена");render()}
function advertise(){if(state.money<10000)return toast("Не хватает денег");state.money-=10000;const b=biz();b.profit=Math.round(b.profit*1.2);state.day++;addLog(`Реклама увеличила прибыль бизнеса.`);toast("Реклама сработала 📈");render()}
function sellBusiness(){const b=biz();state.money+=Math.round(b.cost*.6);state.business=null;addLog(`Бизнес ${b.name} продан.`);toast("Бизнес продан");render()}
document.querySelectorAll(".bottom-nav button").forEach(b=>b.onclick=()=>{if(!state.created)return toast("Сначала создай персонажа");state.screen=b.dataset.screen;render()});
document.getElementById("resetBtn").onclick=()=>{if(confirm("Начать новую игру?")){localStorage.removeItem(KEY);location.reload()}};
render();