from flask import Flask

from api.train_controller import train_controller
from api.match_controller import match_controller


if __name__ == "__main__":
    application = Flask(__name__)
    application.register_blueprint(train_controller)
    application.register_blueprint(match_controller)

    application.run(debug=True, port="5000")
