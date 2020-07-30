# Machine Learning to match stack traces to Stack Overflow titles

## To run:
Do note, this code is a bit scattered; I've been learning Python on-the-fly for this, and thus started with code based off of what I had at hand (the scraper) before graduating to using a Jupyter notebook. Thus, it's kind of contained half-and-half between these two.

The initial test data is gotten by running the flask server and running two commands: first, `train/d2v`, followed by `train/d2v/convert`. From there, place the resulting files in the same directory as the Jupyter notebook to be able to run it without issues.

# Starting the flask server:
Install everything in `requirements.txt`, then run `api/main.py` to get it started.

# Anatomy of the server:
The Doc2Vec part all runs through `api/train_controller.py`, which delegates to `service/train_service.py`.
The data processing is handled by `pre_process_json` (which internally calls `pre_process_d2v`). If my memory serves, everything else of importance
is handled by the methods `train_d2v_service` (create dictionaries) and `convert_to_d2v_service` (vectorize data).

# Endpoints for the flask server:
## Create the dictionary

**URL** : `/train/d2v/`

**Method** : `POST`

**Data Constraints** : 

```json
{
        "filenames": [ "Paths to the files containing data to train on", "There can be more than one file" ]
}
```

The data to train on should be a object like as follows:

```json
[
  {
    "title": "title of post 1",
    "trace": "Stack trace that matches post 1"
  },
  {
    "title": "title of post 2",
    "trace": "Stack trace that matches post 2"
  },
  ...
  {
    "title": "title of post N",
    "trace": "Stack trace that matches post N"
  }
]
```

### Success Response
**Code**: `200 OK`

## Create vectors from data:

**URL** : `/train/d2v/convert/`

**Method** : `POST`

**Data Constraints** : 

```json
{
        "filenames": [ "Paths to the files containing data to convert", "There can be more than one file" ]
}
```

The format of the data to convert is identical to the format for the training

### Success Response
**Code**: `200 OK`

Creates files `titles_vec.pkl` and `traces_vec.pkl` in the directory the program was run from (usually the API directory)
