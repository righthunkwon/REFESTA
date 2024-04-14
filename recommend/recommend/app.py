import csv
import os
import numpy as np
import random
import pymysql
from apscheduler.schedulers.background import BackgroundScheduler
from dotenv import load_dotenv
from flask import Flask, request
from sklearn.metrics.pairwise import cosine_similarity

load_dotenv()
current_directory = os.getcwd()
DB_HOST = os.environ.get('DB_HOST')
DB_USER = os.environ.get('DB_USER')
DB_PASSWORD = os.environ.get('DB_PASSWORD')
app = Flask(__name__)
scheduler = BackgroundScheduler()


def RMSE(y_true, y_pred): # RMSE 함수
    return np.sqrt(np.mean((np.array(y_true)-np.array(y_pred))**2))

# score(RMSE) 계산
def CF(data, sim, member):
    memsim = sim[member]
    memrating = data[member]

    predict = np.zeros_like(memrating)
    for i, rating in enumerate(memrating):
        if rating == 0:
            weighted_sum = 0
            weight_sum = 0
            for j, sim in enumerate(memsim):
                if j != member and data[j][i] != 0:
                    weighted_sum += sim * data[j][i]
                    weight_sum += sim
            if weight_sum != 0:
                predict[i] = weighted_sum / weight_sum
    return predict

def CollaborativeFiltering():
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()
    data = read_from_csv('finaltable.csv')
    for i in range(len(data)):
        for j in range(len(data[i])):
            data[i][j] = int(data[i][j])
    cur.execute("SELECT max(id) from member")
    memx = int(cur.fetchone()[0])
    cur.execute("SELECT COUNT(*) from festival")
    fesx = int(cur.fetchone()[0])

    sim = cosine_similarity(data)
    predict_rating = [[0 for _ in range(fesx)] for _ in range(memx)]
    for i in range(memx):
        predict_rating[i] = CF(data, sim, i)
    save_to_csv(predict_rating, 'CFtable.csv')
    cur.close()
    conn.close()

def save_to_csv(data, filename):

    with open(filename, 'w', newline='') as csvfile:
        csvwriter = csv.writer(csvfile)
        csvwriter.writerows(data)

def read_from_csv(filename):
    with open(filename, 'r', newline='') as csvfile:
        csvreader = csv.reader(csvfile)
        data = [row for row in csvreader]
    return data

def makeusertable():
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()


    #1
    cur.execute("SELECT MAX(id) from member")
    memberN = cur.fetchone()[0]

    cur.execute("SELECT member_id, genre_id FROM prefer_genre")
    membergenre = cur.fetchall()
    membergenretable = [[0 for _ in range(9)] for _ in range(memberN)]
    for i in membergenre:
        membergenretable[i[1]-1][i[0]-1]+=1;

    #2
    cur.execute("SELECT COUNT(*) from festival")
    festivalN = cur.fetchone()[0]
    lineupgenretable = [[0  for _ in range(festivalN)] for _ in range(9)]
    for i in range(festivalN):
        cur.execute(f"SELECT artist_id, genre_id FROM artist_genre WHERE artist_id IN (SELECT artist_id FROM festival_lineup WHERE festival_id = {i+1})")
        lineup = cur.fetchall()
        for j in lineup:
            lineupgenretable[j[1]-1][i]+=1
    cur.execute("SELECT name FROM festival")
    festivalName = cur.fetchall()
    for i in range(festivalN):
        if "재즈" in festivalName[i]:
            lineupgenretable[8][i]+=20
            for j in range(9):
                if j==8: continue
                lineupgenretable[j][i]-=15
        if "힙합" in festivalName[i]:
            lineupgenretable[2][i]+=20
            for j in range(9):
                if j==2: continue
                lineupgenretable[j][i]-=15
        if "락" in festivalName[i]:
            lineupgenretable[5][i]+=20
            for j in range(9):
                if j==5: continue
                lineupgenretable[j][i]-=15
        if "인디" in festivalName[i]:
            lineupgenretable[4][i]+=20
            for j in range(9):
                if j==4: continue
                lineupgenretable[j][i]-=15

    #1+2
    memberfestivalgenretable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in range(memberN):
        for j in range(festivalN):
            for k in range(9):
                memberfestivalgenretable[i][j]+=membergenretable[i][k]*lineupgenretable[k][j]

    save_to_csv(memberfestivalgenretable, 'memberfestivalgenretable.csv')

    #3
    cur.execute("SELECT member_id,artist_id,is_liked FROM artist_like")
    artistlike = cur.fetchall()
    memberartistliketable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in artistlike:
        cur.execute(f"select festival_id from festival_lineup where artist_id={i[1]}")
        for j in cur.fetchall():
            if not bool(int.from_bytes(i[2], byteorder='big')):
                memberartistliketable[i[0]-1][j[0]-1]-=10
            else:
                memberartistliketable[i[0]-1][j[0]-1]+=20
    for i in range(memberN):
        cur.execute(f"SELECT artist_id,preference FROM member_artist_preference where member_id={i+1}")
        for j in cur.fetchall():
            cur.execute(f"select festival_id from festival_lineup where artist_id={j[0]}")
            for k in cur.fetchall():
                memberartistliketable[i][k[0]-1]+=j[1]

    save_to_csv(memberartistliketable, 'memberartistliketable.csv')

    #4
    cur.execute("SELECT member_id,festival_id,is_liked FROM festival_like")
    memberfestivalliketable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in cur.fetchall():

        if not bool(int.from_bytes(i[2], byteorder='big')):
            memberfestivalliketable[i[0]-1][i[1]-1]-=15
        else:
            memberfestivalliketable[i[0]-1][i[1]-1]+=40
    for i in range(memberN):
        cur.execute(f"SELECT festival_id,preference FROM member_festival_preference where member_id={i + 1}")
        for j in cur.fetchall():
            memberfestivalliketable[i][j[0]-1]+=j[1]

    save_to_csv(memberfestivalliketable, 'memberfestivalliketable.csv')

    #5
    cur.execute("SELECT m.member_id, m.song_id, m.preference, f.festival_id FROM member_song_preference m JOIN festival_setlist f ON m.song_id = f.song_id")
    membersongliketable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in cur.fetchall():
        membersongliketable[i[0]-1][i[3]-1]+=i[2]

    save_to_csv(membersongliketable, 'membersongliketable.csv')

    #6
    cur.execute("select member_id,festival_id from review where is_deleted=0;")
    review = cur.fetchall()
    memberreviewtable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in review:
        memberreviewtable[i[0]-1][i[1]-1]+=1

    save_to_csv(memberreviewtable, 'memberreviewtable.csv')

    #7
    cur.execute("SELECT member_id, festival_id FROM reservation WHERE status = 'SUCCESS'")
    reserv = cur.fetchall()
    memberreservtable = [[0 for _ in range(festivalN)] for _ in range(memberN)]

    for i in reserv:
        memberreservtable[i[0]-1][i[1]-1]=1

    save_to_csv(memberreservtable, 'memberreservtable.csv')

    #final
    finaltable = [[0 for _ in range(festivalN)] for _ in range(memberN)]
    for i in range(memberN):
        for j in range(festivalN):
            finaltable[i][j] = memberreservtable[i][j]+memberreviewtable[i][j]+membersongliketable[i][j]+memberfestivalliketable[i][j]+memberartistliketable[i][j]+memberfestivalgenretable[i][j]
    save_to_csv(finaltable, 'finaltable.csv')
    cur.close()
    conn.close()
    CollaborativeFiltering()

def memberrecommend(member):
    print(member)
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()
    cur.execute("DELETE FROM member_festival WHERE member_id = %s", (member,))
    conn.commit()

    cur.execute("DELETE FROM member_artist WHERE member_id = %s", (member,))
    conn.commit()

    finalrecommend = read_from_csv('finaltable.csv')

    cfrecommend = read_from_csv('CFtable.csv')
    for i in range(len(finalrecommend)):
        for j in range(len(finalrecommend[i])):
            finalrecommend[i][j] = int(finalrecommend[i][j])
            cfrecommend[i][j] = int(cfrecommend[i][j])

    for i in range(len(finalrecommend)):
        for j in range(len(finalrecommend[i])):
            finalrecommend[i][j] = finalrecommend[i][j] + int(cfrecommend[i][j] / 100)

    sorted = np.argsort(finalrecommend[member-1])[::-1]
    for i in sorted:
        cur.execute("INSERT INTO member_festival (member_id, festival_id) VALUES (%s, %s)", (member, str(i+1)))
    conn.commit()

    recomartist=[]
    for i in sorted:
        cur.execute(f"SELECT artist_id FROM festival_lineup where festival_id = {i+1}")
        artistTemp=[]
        for j in cur.fetchall():
            artistTemp.append(j[0])
        random.shuffle(artistTemp)
        for j in artistTemp:
            if j not in recomartist:
                recomartist.append(j)
    for i in recomartist:
        cur.execute("INSERT INTO member_artist (member_id, artist_id) VALUES (%s, %s)", (member, str(i)))
    conn.commit()
    cur.close()
    conn.close()

@scheduler.scheduled_job('cron', day_of_week='*', hour=4)
def initialization():
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()
    makeusertable()
    cur.execute("SELECT MAX(id) FROM member")
    memberN = cur.fetchone()[0]
    for i in range(memberN):
        memberrecommend(i+1)
    cur.close()
    conn.close()
    return "추천 갱신 완료"

@app.route('/recommend', methods=['GET'])
def recommendusers():
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()
    makeusertable()
    cur.execute("SELECT MAX(id) FROM member")
    memberN = cur.fetchone()[0]
    for i in range(memberN):
        memberrecommend(i+1)
    cur.close()
    conn.close()
    return "Success initialization"


@app.route('/recommend', methods=['POST'])
def register():
    conn = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db='refesta', charset='utf8')
    cur = conn.cursor()
    userid = request.form.get('userId')
    print(userid)
    cur.execute("SELECT * FROM member WHERE id = %s", (userid,))
    row = cur.fetchone()
    if row is None:
        return "유저가없다"
    makeusertable()
    memberrecommend(int(userid))
    cur.close()
    conn.close()
    return "추천 완료"

if __name__ == '__main__':
    makeusertable()
    scheduler.start()
    app.run(host='0.0.0.0', port=8082)