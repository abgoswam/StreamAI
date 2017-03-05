from flask import Flask, Response

# Creating a flask app
app = Flask(__name__)

import csv

import random
import time

from kafka import KafkaProducer


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


@app.route('/')
def plotly():
    """This reads the from winequality-red-scaled.csv for now .

    Returns actual html as a response red from the file
    """

    dataset = read_input_file("./data_files/winequality-red-scaled.csv")

    producer = KafkaProducer(bootstrap_servers='localhost:9092')

    for line in dataset:

        one_line = []
        for col in line:
            one_line.append(col)

        one_line = ','.join(str(i) for i in one_line)
        r = random.randint(1, 10)  # random sleep time between [1,10] seconds
        time.sleep(r)
        producer.send("teststreamai1", str.encode(one_line))

        producer.flush()

    from bokeh.plotting import figure, show, output_file
    from bokeh.sampledata.iris import flowers

    import pandas as pd

    df = pd.read_csv('./data_files/winequality-red-scaled.csv', sep=',')

    train = df[:1000]
    test = df[1000:]

    train_X, train_Y = train.drop(['quality'], axis=1), train['quality']
    test_X, test_Y = test.drop(['quality'], axis=1), test['quality']

    print(train_Y)

    train_Y = [(float(x) + random.uniform(0, 1)) for x in train_Y]

    train_Y_1 = train_Y[:500]
    train_Y_2 = train_Y[500:]

    colors = ['red' for x in range(1000)]

    p = figure(title="Flask Stack")
    p.xaxis.axis_label = 'Actual Value'
    p.yaxis.axis_label = 'Predicted Value'

    p.circle(train_Y_1, train_Y_2,
             color=colors, fill_alpha=0.2, size=10)

    try:
        output_file("log_lines.html", title="iris.py example")
    except Exception as e:
        print(e.args)

    show(p)

    return Response(open('log_lines.html').read(),
                    mimetype="text/html")
