from flask import Flask
from flask_cors import CORS
from api.db_controller import db_controller
from api.scrape_controller import scrape_controller

if __name__ == "__main__":
    application = Flask(__name__)
    application.register_blueprint(db_controller)
    application.register_blueprint(scrape_controller)

    application.run(debug=True, port="5000")