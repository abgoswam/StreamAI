import csv
import random
import time

import flask
from flask import Flask, Response
from flask import Flask, jsonify
from jinja2 import Template


from bokeh.plotting import figure
from bokeh.models import AjaxDataSource
from bokeh.embed import components
from bokeh.resources import INLINE
from bokeh.util.string import encode_utf8

# Creating a flask app
app = Flask(__name__)

from kafka import KafkaProducer


def stream_template(template_name, **context):
    """Utility function to test the flask
    streaming capability

    :param template_name: name of the template
    :param context: context streaming .
    :return:
    """
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

script = ''
div = ''


def generate_next_row():
    """Utility for asynchronous request to
    api for sending the data to kafka

    Yields the one row from wine file at the time .
    """
    dataset = read_input_file("./data_files/winequality-red-scaled.csv")

    for line in dataset:

        one_line = []
        for col in line:
            one_line.append(col)

        one_line = ','.join(str(i) for i in one_line)

        r = random.randint(1, 10)  # random sleep time between [1,10] seconds
        time.sleep(r)

        producer = KafkaProducer(bootstrap_servers='localhost:9092')

        producer.send('StreamAI', str.encode(one_line))
        producer.flush()

        yield one_line.join("Send this data to moron abo \n")


@app.route("/data", methods=['POST'])
def send_data_to_bokeh_plot():
    """ This sends to data to bokeh plot currently .
    Usually there is a internal api called from an
    exposed endpoint .
    """
    global count
    x = count
    y = random.randint(1, 10)
    count += 1

    return flask.jsonify(x=[x], y=[y])


@app.route('/')
def send_data_to_kafka_consumer():
    """This reads the from winequality-red-scaled.csv for now .

    """
    return Response(generate_next_row(), mimetype="text/html")


# Simple html / javascript template that could will be
# replaced by bokek with embeded plot javascript
# and css .
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


@app.route('/plot_demo')
def bokeh_demo():
    """End point demoing the real time injection of the data
    to bokeh plots

    Returns streaming response from the bokeh server .
    """

    source = AjaxDataSource(data_url="http://localhost:5000/data",
                            polling_interval=3000, mode='append')

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
