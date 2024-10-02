from flask import Flask
from flask_restful import Resource, Api
import psycopg2
import psycopg2.extras

app = Flask(__name__)
api = Api(app)

class Groups (Resource):
    groups_list=[]

    def get (self):
        self.groups_list = []
        self.load_groups()
        return self.groups_list
    
    def load_groups (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('SELECT * FROM groups') 
            for row in cursor:
                id = row['id']
                name = row['title']
                url = row['url']
                self.groups_list.append({'id': id, 'name': name, 'url': url})
            
            conn.close()
        except Exception as e:
            print (str(e))
    
api.add_resource(Groups, '/groups')

class times (Resource):
    time_list=[]

    def get (self):
        self.time_list = []
        self.load_times()
        return self.time_list
    
    def load_times (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('SELECT * FROM times') 
            for row in cursor:
                id = row['id']
                name = row['time']
                
                self.time_list.append({'id': id, 'name': name})
            
            conn.close()
        except Exception as e:
            print (str(e))
    
api.add_resource(times, '/times')

class teachers (Resource):
    teach_list=[]

    def get (self):
        self.teach_list = []
        self.load_teach()
        return self.teach_list
    
    def load_teach (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('SELECT * FROM teachers') 
            for row in cursor:
                id = row['id']
                name = row['fio']
                
                self.teach_list.append({'id': id, 'name': name})
            
            conn.close()
        except Exception as e:
            print (str(e))
    
api.add_resource(teachers, '/teach')

class rooms (Resource):
    rooms_list=[]

    def get (self):
        self.rooms_list = []
        self.load_rooms()
        return self.rooms_list
    
    def load_rooms (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('SELECT * FROM rooms') 
            for row in cursor:
                id = row['id']
                name = row['num']
                self.rooms_list.append({'id': id, 'name': name})
            
            conn.close()
        except Exception as e:
            print (str(e))
    
api.add_resource(rooms, '/rooms')

class Days (Resource):
    days_list=[]

    def get (self):
        self.days_list = []
        self.load_days()
        return self.days_list
    
    def load_days (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute(''''SELECT * FROM public.days
ORDER BY id ASC ''')
            for row in cursor:
                id = row['id']
                name = row['name']
                self.days_list.append({'id': id, 'name': name})
            
            conn.close()
        except Exception as e:
            print (str(e))
    
api.add_resource(Days, '/days')

class Disc (Resource):
    disc_list=[]

    def get (self):
        self.disc_list = []
        self.load_disc()
        return self.disc_list
    
    def load_disc (self):

        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('SELECT * FROM disciplines') 
            for row in cursor:
                id = row['id']
                name = row['name']
                self.disc_list.append({'id': id, 'name': name})
            
            conn.close()
        except Exception as e:
            print (str(e))
    


class Schedule(Resource):
    schedule_list = []

    def get(self):
        self.schedule_list = []
        self.load_schedule()
        return self.schedule_list

    def load_schedule(self):
        try:
            conn = psycopg2.connect(host='localhost', user='postgres', password='Zmzvsig98', dbname='institut', port='5432')
            cursor = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
            cursor.execute('''
                SELECT 
    d.name AS "Дисциплина",
    t.time AS "Время",
    dy.name AS "День недели",
    te.fio AS "Преподаватель",
    c.num AS "Кабинет",
    gr.title AS "Группа"
FROM 
    schedule
    JOIN disciplines d ON schedule.id_discipline = d.id
    JOIN times t ON schedule.id_time = t.id
    JOIN days dy ON schedule.id_day = dy.id
    JOIN teachers te ON schedule.id_teacher = te.id
    JOIN rooms c ON schedule.id_room = c.id
    JOIN groups gr ON schedule.id_group = gr.id;
            ''')
            for row in cursor:
                discipline = row['Дисциплина']
                time = row['Время']
                day = row['День недели']
                teacher = row['Преподаватель']
                cabinet = row['Кабинет']
                group_name = row['Группа']
                self.schedule_list.append({
                    'discipline': discipline,
                    'time': time,
                    'day': day,
                    'teacher': teacher,
                    'cabinet': cabinet,
                    'group_name': group_name
                })
            conn.close()
        except Exception as e:
            print(str(e))

api.add_resource(Schedule, '/schedule')
api.add_resource(Disc, '/disc')


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')

	
