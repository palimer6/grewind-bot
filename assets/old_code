const botconfig = require("./botconfig.json");
const Discord = require("discord.js");

const bot = new Discord.Client({disableEveryone: true});

bot.on("ready", async () => {
  console.log(`${bot.user.username} is online!`)
  bot.user.setActivity("type !help for commands", {type: "WATCHING"});
});

bot.on("message", async message => {
  if(message.author.bot) return;
//  if(message.channel.type === "dm") return;

  let prefix = botconfig.prefix;
  let messageArray = message.content.split(" ");
  let cmd = messageArray[0];
  let args = messageArray.slice(1);


  if(cmd === `${prefix}botinfo`){

    let bicon = bot.user.displayAvatarURL;
    let botembed = new Discord.RichEmbed()
    .setDescription("Bot Information")
    .setColor("#Eb8c3f")
    .setThumbnail(bicon)
    .addField("Bot Name", bot.user.username)
    .addField("Created On", bot.user.createdAt);

    return message.channel.send(botembed);

  }

  /*if(cmd === `${prefix}serverinfo`){

    let sicon = message.guild.iconURL;
    let serverembed = new Discord.RichEmbed()
    .setDescription("Server Information")
    .setColor("#Eb8c3f")
    .setThumbnail(sicon)
    .addField("Server Name", message.guild.name)
    .addField("Created On", message.guild.createdAt)
    .addField("You Joined", message.member.joinedAt)
    .addField("Total Members", message.guild.memberCount);

    return message.channel.send(serverembed);
  }*/






  if(cmd === `${prefix}ping`){
    return message.channel.send("Pong!");

  }

  function tempConvert(from, to, q){
    if(from === to) return q;
    if(to === "k"){
      switch(from){
        case "c": return (273.15 + q);
        case "f": return (q + 459.67) * (5/9);
      }
    }
    if(from === "k"){
      switch(to){
        case "c": return q - 273.15;
        case "f": return (q * (9/5)) - 459.67;
      }
    }
    return tempConvert("k", to, tempConvert(from, "k", q));
  }

  function massConvert(from, to, q){
    if(from === to) return q;
    if(to === "kg"){
      switch(from){
        case "mg": return q * 0.000001;
        case "g": return q * 0.001;
        case "oz": return q * 0.02834952;
        case "lb": return q * 0.45359237;
      }
    }
    if(from === "kg"){
      switch(to){
        case "mg": return q / 0.000001;
        case "g": return q / 0.001;
        case "oz": return q / 0.02834952;
        case "lb": return q / 0.45359237;
      }
    }
    return massConvert("kg", to, massConvert(from, "kg", q));
  }

  function lengthConvert(from, to, q){
    if (from === to) return q;
    if (to === "m"){
      switch(from){
        case "mm": return q * 0.001;
        case "cm": return q * 0.01;
        case "km": return q * 1000;
        case "in": return q * 0.0254;
        case "ft": return q * 0.3048;
        case "yd": return q * 0.9144;
        case "mi": return q * 1609.344;
      }
    }
    if (from === "m"){
      switch(to){
        case "mm": return q / 0.001;
        case "cm": return q / 0.01;
        case "km": return q / 1000;
        case "in": return q / 0.0254;
        case "ft": return q / 0.3048;
        case "yd": return q / 0.9144;
        case "mi": return q / 1609.344;
      }
    }
    return lengthConvert("m", to, lengthConvert(from, "m", q));
  }

  function areaConvert(from, to, q){
    if (from === to) return q;
    if (to === "sqm"){
      switch(from){
        case "sqcm": return q * 0.0001;
        case "sqkm": return q * 1000000;
        case "ha": return q * 10000;
        case "sqin": return q * 0.00064516;
        case "sqft": return q * 0.09290304;
        case "acre": return q * 4046.8564224;
        case "sqmi": return q * 2589988.1103;
      }
    }
    if (from === "sqm"){
      switch(to){
        case "sqcm": return q / 0.0001;
        case "sqkm": return q / 1000000;
        case "ha": return q / 10000;
        case "sqin": return q / 0.00064516;
        case "sqft": return q / 0.09290304;
        case "acre": return q / 4046.8564224;
        case "sqmi": return q / 2589988.1103;
      }
    }
    return areaConvert("sqm", to , areaConvert(from, "sqm", q));
  }

  function volumeConvert(from, to, q){
    if (from === to) return q;
    if (to === "cbm"){
      switch(from){
        case "ml": return q * 0.000001;
        case "l": return q * 0.001;
        case "oz": return q * 0.000029573529563;
        case "cp": return q * 0.0002365882365;
        case "pt": return q * 0.000473176473;
        case "qt": return q * 0.000946352946;
        case "gal": return q * 0.003785411784;
      }
    }
    if(from === "cbm"){
      switch(to){
        case "ml": return q / 0.000001;
        case "l": return q / 0.001;
        case "oz": return q / 0.000029573529563;
        case "cp": return q / 0.0002365882365;
        case "pt": return q / 0.000473176473;
        case "qt": return q / 0.000946352946;
        case "gal": return q / 0.003785411784;
      }
    }
    return volumeConvert("cbm", to, volumeConvert(from, "cbm", q));
  }

  function speedConvert(from, to, q){
    if (from === to) return q;
    if (to === "m/s"){
      switch(from){
        case "km/h": return q * (5/18);
        case "ft/s": return q * 0.3048;
        case "mph": return q * 0.44704;
      }
    }
    if (from === "m/s"){
      switch(to){
        case "km/h": return q / (5/18);
        case "ft/s": return q / 0.3048;
        case "mph": return q / 0.44704;
      }
    }
    return speedConvert("m/s", to, speedConvert(from, "m/s", q));
  }

  function timeConvert(from, to, q){
    if (from === to) return q;
    if (to === "hr"){
      switch(from){
        case "s": return q / 3600;
        case "min": return q / 60;
        case "d": return q * 24;
        case "wk": return q * 168;
        case "mn": return q * 720;
        case "yr": return q * 8765.82;
      }
    }
    if (from === "hr"){
      switch(to){
        case "s": return q * 3600;
        case "min": return q * 60;
        case "d": return q / 24;
        case "wk": return q / 168;
        case "mn": return q / 720;
        case "yr": return q / 8765.82;
      }
    }
    return timeConvert("hr", to, timeConvert(from, "hr", q));
  }

  if(cmd === `${prefix}convert`){
    var base = args[0];
    var from = args[1];
    var to = args[2];
    var q = parseFloat(args[3]);
    var result;
    //return message.channel.send("" + base + " " + from + " " + to + " " + q);
    switch(base){
      case "temp": result = tempConvert(from, to, q); break;
      case "mass": result = massConvert(from, to, q); break;
      case "distance": result = lengthConvert(from, to, q); break;
      case "area": result = areaConvert(from, to, q); break;
      case "volume": result = volumeConvert(from, to, q); break;
      case "speed": result = speedConvert(from, to, q); break;
      case "time": result = timeConvert(from, to, q); break;
      default: return message.channel.send("Invalid base: " + base);
    }
    return message.channel.send("" + q + unitDB(from) + " is " + (Math.round(result * 1000) / 1000) + unitDB(to));

  }

  if(cmd === `${prefix}timezones`){
    var baseZone = args[0];
    var time = parseInt(args[1]);
    switch(baseZone){
      case "PST": return message.channel.send("PST: " + clock(0+time) + "\nMST: " + clock(1+time) + "\nEST: " + clock(3+time) + "\nGMT: " + clock(8+time) + "\nCET: " + clock(time+9));
      case "MST": return message.channel.send("PST: " + clock(time-1) + "\nMST: " + clock(0+time) + "\nEST: " + clock(2+time) + "\nGMT: " + clock(7+time) + "\nCET: " + clock(8+time));
      case "EST": return message.channel.send("PST: " + clock(time-3) + "\nMST: " + clock(time-2) + "\nEST: " + clock(0+time) + "\nGMT: " + clock(5+time) + "\nCET: " + clock(6+time));
      case "GMT": return message.channel.send("PST: " + clock(time-8) + "\nMST: " + clock(time-7) + "\nEST: " + clock(time-5) + "\nGMT: " + clock(0+time) + "\nCET: " + clock(1+time));
      case "CET": return message.channel.send("PST: " + clock(time-9) + "\nMST: " + clock(time-8) + "\nEST: " + clock(time-6) + "\nGMT: " + clock(time-1) + "\nCET: " + clock(0+time));
    }
  }

  /*if(cmd === `${prefix}clock`){
    var var1 = parseInt(args[0]);
    return message.channel.send(clock(var1));
  }*/

  function clock(time){
    if (time<0) return (24+parseInt(time));
    if (time>23) return (parseInt(time)%24)
    return time;
  }

  function unitDB(unit){
    switch(unit){
      case "k": return "K";
      case "c": return "°C";
      case "f": return "°F";

      case "kg": return "kg";
      case "mg": return "mg";
      case "g": return "g";
      case "oz": return "oz";
      case "lb": return "lb";

      case "m": return "m";
      case "mm": return "mm";
      case "cm": return "cm";
      case "km": return "km";
      case "in": return "in";
      case "ft": return "ft";
      case "yd": return "yd";
      case "mi": return "mi";

      case "sqm": return "m²";
      case "sqcm": return "cm²";
      case "sqkm": return "km²";
      case "ha": return "ha";
      case "sqin": return "in²";
      case "sqft": return "ft²";
      case "acre": return "acre";
      case "sqmi": return "mi²";

      case "cbm": return "m³";
      case "ml": return "ml";
      case "l": return "l";
      //case "oz": return "oz";
      case "cp": return "cp";
      case "pt": return "pt";
      case "qt": return "qt";
      case "gal": return "gal";

      case "m/s": return "m/s";
      case "km/h": return "km/h";
      case "ft/s": return "ft/s";
      case "mph": return "mph";

      case "hr": return " hour(s)";
      case "s": return " second(s)";
      case "min": return " minute(s)";
      case "d": return " day(s)";
      case "wk": return " week(s)";
      case "mn": return " month(s)";
      case "yr": return " year(s)";

      default: return "N/A";
    }
  }

  if(cmd === `${prefix}help`){
    return message.author.send("```EVERYTHING IS CASE SENSITIVE\nThe square brackets [] are not to be used in the final command\n!help\n\tDMs you this list\n!botinfo\n\tgives info about Grewind Bot\n!ping\n\treturns a message to let you know the bot is up\n!timezones [zone] [hour]\n\tconverts a specific hour from a grewind-timezone(PST, MST, EST, GMT, CET) into all others\n\t\tonly uses 24-hour-system\n!convert [base] [starting unit] [result unit] [value]\n\tlet\'s you convert many different units that we always have trouble with\n\tfor more info on what\'s possible, use\n!converthelp\n\tDMs you a list of what bases of conversion are currently supported, and what units within them```");
  }

  if(cmd === `${prefix}converthelp`){
    return message.author.send("```Currency will never be supported as it's constantly changing.\nSyntax:\n\t!convert [base] [starting unit] [result unit] [value]\nTemperature:\n\ttemp\n\t\tKelvin:\n\t\t\tk\n\t\tCelsius:\n\t\t\tc\n\t\tFahrenheit:\n\t\t\tf\nMass:\n\tmass\n\t\tKilogram:\n\t\t\tkg\n\t\tGram:\n\t\t\tg\n\t\tMilligram:\n\t\t\tmg\n\t\tOunce:\n\t\t\toz\n\t\tPound:\n\t\t\tlb\nDistance:\n\tdistance\n\t\tMeter:\n\t\t\tm\n\t\tKilometer:\n\t\t\tkm\n\t\tCentimeter:\n\t\t\tcm\n\t\tMillimeter:\n\t\t\tmm\n\t\tInch:\n\t\t\tin\n\t\tFoot:\n\t\t\tft\n\t\tMile:\n\t\t\tmi\n\t\tYard:\n\t\t\tyd\nArea:\n\tarea\n\t\tSquare Meter:\n\t\t\tsqm\n\t\tSquare Centimeter:\n\t\t\tsqcm\n\t\tSquare Kilometer:\n\t\t\tsqkm\n\t\tHectare:\n\t\t\tha\n\t\tSquare Inch:\n\t\t\tsqin\n\t\tSquare Foot:\n\t\t\tsqft\n\t\tAcre:\n\t\t\tacre\n\t\tSquare Mile:\n\t\t\tsqmi\nVolume: (These all refer to US Fluid Units)\n\tvolume\n\t\tCubic Meter:\n\t\t\tcbm\n\t\tLiter:\n\t\t\tl\n\t\tMilliliter:\n\t\t\tml\n\t\tOunce:\n\t\t\toz\n\t\tCup:\n\t\t\tcp\n\t\tPint:\n\t\t\tpt\n\t\tQuart:\n\t\t\tqt\n\t\tGallon:\n\t\t\tgal\nSpeed:\n\tspeed\n\t\tMeters per Second:\n\t\t\tm/s\n\t\tKilometers per Hour:\n\t\t\tkm/h\n\t\tMiles per Hour:\n\t\t\tmph\n\t\tFeet per Second:\n\t\t\tft/s\nTime:\n\ttime\n\t\tSecond:\n\t\t\ts\n\t\tMinute:\n\t\t\tmin\n\t\tHour:\n\t\t\thr\n\t\tDay:\n\t\t\td\n\t\tWeek:\n\t\t\twk\n\t\tMonth:\n\t\t\tmn\n\t\tYear:\n\t\t\tyr```");
  }




});

bot.login(botconfig.token);
