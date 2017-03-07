import sys, nflgame;
from pprint import pprint;


year = sys.argv[1];
week = sys.argv[2];
path = sys.argv[3];

weekfull = week;
if (int(weekfull) < 10):
    weekfull = "0" + weekfull;

print "Week : ",week;
print "WeekFull : ",weekfull;
#week = "01";
#
games = nflgame.games(int(year), int(week));
players = nflgame.combine(games);
players.csv('%sweek%s.csv' %(path,weekfull));
print "Week ",week, " stats saved.";
#for p in players.rushing().sort("rushing_yds").limit(10):
#for p in players:
    #pprint (vars(p));
    #print p;

string1 = "%sweek%skickers.csv" %(path,weekfull)
f = open(string1,'w')
for game in games:
    try:
        for p in game.drives.plays():
            if p.kicking_fgm_yds > 0:
#                print (vars(p))
#                print p.kicking_fgm_yds
                players = p._Play__players;
                for player in players:
#                    print player
                    yards = p.kicking_fgm_yds
                    writeString = player
                    writeString += ",%d" %yards 
                    writeString += "\n"
                    f.write(writeString)
    except ValueError:
        print "Error with value...skipping"
f.close()
print "Kicker stats for week " + week + " are done."


