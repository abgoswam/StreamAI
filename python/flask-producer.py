
import flask
from flask import Flask, Response

from flask import Flask, jsonify
from jinja2 import Template
import math

from bokeh.plotting import figure
from bokeh.models import AjaxDataSource
from bokeh.embed import components
from bokeh.resources import INLINE
from bokeh.util.string import encode_utf8

# Creating a flask app
app = Flask(__name__)

import csv

import random
import time
import os
from kafka import KafkaProducer


def stream_template(template_name, **context):
    app.update_template_context(context)
    t = app.jinja_env.get_template(template_name)
    rv = t.stream(context)
    rv.enable_buffering(5)
    return rv


def load_csv(filename):
    with open(filename) as f:
        csv_reader = csv.reader(f, delimiter=',')
        header = csv_reader.__next__()
        dataset = list()
        for line in csv_reader:
            if not line:
                continue
            dataset.append(line)

    return dataset


def read_input_file(input_file):
    dataset = load_csv(input_file)
    return dataset

count = 0
train_Y_1 = []
train_Y_2 = []

x, y = 0.0, 0.0

script =''
div =''

def generate_next_row() :
    dataset = read_input_file("./data_files/winequality-red-scaled.csv")

    for line in dataset:

        one_line = []
        for col in line:
            one_line.append(col)

        one_line = ','.join(str(i) for i in one_line)
        all_values = one_line.split(',')
        yield float(all_values[len(all_values) - 1])


next_data_row = generate_next_row()


@app.route("/data", methods=['POST'])
def send_data_to_sgd():
    global count
    global train_Y_1
    global train_Y_2
    global script
    global div

    # producer = KafkaProducer(bootstrap_servers='localhost:9092')
    # producer.send(topic_name, str.encode(data))
    # producer.flush()

    from bokeh.plotting import figure, show, output_file
    global x

    # if len(train_Y_1) > 1:
    #     x = train_Y_1[len(train_Y_1)-1]

    x = count
    y = random.randint(1,10)
    count = count +1
    # y = x + random.randrange(0, 1)

    print("Print X and Y : ")
    print((x, y))


    return flask.jsonify(x=[x], y=[y])

    # train_Y_1.append(float(all_values[len(all_values)-1]))
    # train_Y_2.append(float(all_values[len(all_values)-1]))

    # print("--------Debug Train Ys -----")
    # print(train_Y_1)
    # print(len(train_Y_1))
    # print(train_Y_2)
    # print(len(train_Y_2))
    #
    # count= count + 1
    #
    # colors = ['red' for x in range(len(train_Y_1))]
    #
    # p = figure(title="Flask Stack")
    # p.xaxis.axis_label = 'Actual Value'
    # p.yaxis.axis_label = 'Predicted Value'
    #
    # p.circle(train_Y_1, train_Y_2,
    #          color=colors, fill_alpha=0.2, size=10)
    #
    # from bokeh.embed import components
    # script, div = components(p)


@app.route('/')
def plotly():
    """This reads the from winequality-red-scaled.csv for now .

    Returns actual html as a response red from the file
    """

    data_file_winequality = os.path.join(os.environ['STREAMAI_HOME'], 'python/data_files/winequality-red-scaled.csv')
    dataset = read_input_file(data_file_winequality)

    producer = KafkaProducer(bootstrap_servers='localhost:9092')

    for line in dataset:

        one_line = []
        for col in line:
            one_line.append(col)

        one_line = ','.join(str(i) for i in one_line)


        # r = random.randint(1, 10)  # random sleep time between [1,10] seconds
        # time.sleep(r)
       # send_data_to_sgd("AboPunk", one_line)
        all_values = one_line.split(',')
        train_Y_1.append(float(all_values[len(all_values)-1]))
        producer = KafkaProducer(bootstrap_servers='localhost:9092')
        producer.send('AboPunk', str.encode(one_line))
        producer.flush()

    global train_Y_1
    global train_Y_2
    global script
    global div

    return Response(stream_template('log_lines.html', rows=len(train_Y_1)))


# @app.route("/data", methods=['POST'])
# def get_x():
#     global x, y
#     x = x + 0.1
#     y = math.sin(x)
#     return flask.jsonify(x=[x], y=[y])

template = Template('''<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Streaming Example</title>
        {{ js_resources }}
        {{ css_resources }}
    </head>
    <body>
    {{ plot_div }}
    {{ plot_script }}
    </body>
</html>
''')


@app.route('/second_shot')
def second_shot():

        streaming = True
        source = AjaxDataSource(data_url="http://localhost:5000/data",
                                polling_interval=3000, mode='append')

        dataset = read_input_file("./data_files/winequality-red-scaled.csv")


        producer = KafkaProducer(bootstrap_servers='localhost:9092')


        # for line in dataset:
        #
        #     one_line = []
        #     for col in line:
        #         one_line.append(col)
        #
        #     one_line = ','.join(str(i) for i in one_line)
        #
        #     # r = random.randint(1, 10)  # random sleep time between [1,10] seconds
        #     # time.sleep(r)
        #     global train_Y_1
        #     global train_Y_2
        #     # send_data_to_sgd("AboPunk", one_line)
        #     all_values = one_line.split(',')
        #     train_Y_1.append(float(all_values[len(all_values) - 1]))
        #
        #     # r = random.randint(1, 10)
        #     # time.sleep(1)
        #     # producer = KafkaProducer(bootstrap_servers='localhost:9092')
        #     # producer.send('AboPunk', str.encode(one_line))
        #     # producer.flush()


        source.data = dict(x=[], y=[])

        fig = figure(title="Streaming Example")
        fig.line('x', 'y', source=source)

        js_resources = INLINE.render_js()
        css_resources = INLINE.render_css()

        script, div = components(fig, INLINE)

        html = template.render(
                plot_script=script,
                plot_div=div,
                js_resources=js_resources,
                css_resources=css_resources
            )

        return encode_utf8(html)



