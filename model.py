import tensorflow as tf
from tensorflow.keras import Sequential
from tensorflow.keras.layers import Activation, Dropout, Flatten, Dense, Conv2D, MaxPooling2D
from model1.keras_ssd300 import ssd_300
from keras.optimizers import Adam

def create_model():
    shape_img = (150,150,3)

    model = Sequential()

    model.add(Conv2D(filters=32, kernel_size=(3,3),input_shape=shape_img, activation='relu', padding = 'same'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(filters=64, kernel_size=(3,3),input_shape=shape_img, activation='relu', padding = 'same'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Conv2D(filters=64, kernel_size=(3,3),input_shape=shape_img, activation='relu', padding = 'same'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Flatten())

    model.add(Dense(128))
    model.add(Activation('relu'))
    model.add(Dropout(0.2))

    model.add(Dense(15))
    model.add(Activation('softmax'))

    model.compile(loss='categorical_crossentropy',optimizer='adam',metrics=['accuracy'])

    return model

def create_ssd():
    model = ssd_300(image_size=(300, 300, 3),
                n_classes=4,
                mode='inference',
                l2_regularization=0.0005,
                scales=[0.07, 0.15, 0.33, 0.51, 0.69, 0.87, 1.05],
                aspect_ratios_per_layer=[[1.0, 2.0, 0.5],[1.0, 2.0, 0.5, 3.0, 1.0/3.0],[1.0, 2.0, 0.5, 3.0, 1.0/3.0],[1.0, 2.0, 0.5, 3.0, 1.0/3.0],[1.0, 2.0, 0.5],[1.0, 2.0, 0.5]],
                two_boxes_for_ar1=True,
                steps=[8, 16, 32, 64, 100, 300],
                offsets=[0.5, 0.5, 0.5, 0.5, 0.5, 0.5],
                clip_boxes=False,
                variances=[0.1, 0.1, 0.2, 0.2],
                normalize_coords=True,
                subtract_mean=[123, 117, 104],
                swap_channels=[2, 1, 0],
                confidence_thresh=0.01,
                iou_threshold=0.45,
                top_k=200,
                nms_max_output_size=400)
    adam = Adam(lr=0.001, beta_1=0.9, beta_2=0.999, epsilon=1e-08, decay=0.0)
    model.compile(optimizer=adam)
    return model

def load_model(MODELTYPE='object'):
    
    if MODELTYPE=='class':
        model=create_model()
        model.load_weights('best_model (1).h5')
        classes=["Apple", "Banana", "Carambola", "Guava", "Kiwi", "Mango", "Orange", "Peach", "Pear", "Persimmon", "Pitaya", "Plum", "Pomegranate", "Tomatoes", "muskmelon"]
    else:
        model=create_ssd()
        model.load_weights('VGG_coco_SSD_300x300_iter_400000_subsampled_4_classes.h5', by_name=True)
        classes=['Background','Orange','Banana','Apple','Broccoli']


    return model,classes
