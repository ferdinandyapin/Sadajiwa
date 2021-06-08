#!usr/bin/python3
from flask import Flask, request, jsonify
import numpy as np
import pickle
import logging
import os
from model import load_model
import cv2

FEATURES_MASK = ['IMPATH']
MODELTYPE='object'
MODEL,labels = load_model(MODELTYPE)


app = Flask(__name__)

@app.route('/', methods=['GET'])
def server_check():
    return "I'M RUNNING!"

@app.route('/predict', methods=['POST'])
def predictor():
    content = request.json
    pred_label={}
    try:
        features = content['IMPATH']
    except:
        logging.exception("The JSON file was broke.")
        return jsonify(status='error', predict=-1)
    impath = os.path.join('img',features)
    img=cv2.imread(impath)
    if MODELTYPE=='class':
        img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        img = cv2.resize(img,(150,150))
        img = np.expand_dims(img,axis=0)
        pred = MODEL.predict(img)
        pred_label['Fruit']=labels[np.argmax(pred[0])]
    else:
        img=cv2.resize(img,(300,300))
        img = np.expand_dims(img,axis=0)
        pred = MODEL.predict(img)
        pred=np.matrix(pred)
        pred=np.array(pred.max(0))
        logging.info(pred)
        if pred[0][1]<0.5:
            pred_label['Fruit']=''
        else:
            pred_label['Fruit']=labels[int(pred[0][0])]

    return jsonify(status='ok', predict=pred_label)

if __name__=='__main__':
    app.run( debug=True, host='127.0.0.1', port=int(os.environ.get('PORT', 8080)) )
