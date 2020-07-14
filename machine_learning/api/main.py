from flask import Flask
from flask_cors import CORS

from api.train_controller import train_controller

if __name__ == "__main__":
    application = Flask(__name__)
    application.register_blueprint(train_controller)

    application.run(debug=True, port="5000")
