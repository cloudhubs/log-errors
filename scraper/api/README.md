# Scraper Home Page

**URL** : `/scrape`

**Method** : `GET`

## Success Response
**Code**: `200 OK`

**Content Example**
``` HTML
<p>Hello welcome to the backend</p>
```

# Start Scraper
**URL** : `/scrape/{python, java, csharp}`

**Method** : `POST`

**Data Constraints** : 

```json
{
    "tags":["Stack Overflow Tags to search", ""]
}
```
**Data Example**
```json
{
    "tags":["dotnet", "core"]
}
```
## Success Response
**Code**: `200 OK`

**Content Example**
```
{
    "tags":["dotnet", "core"]
}
```

# Scrape Parent Links
**URL** : `/scrape-meta`

**Method** : `GET`

## Success Response
**Code**: `200 OK`

**Content Example**
```json
{
  "items": [
    {
      "tags": [
        "c++",
        "c",
        "operators",
        "code-formatting",
        "standards-compliance"
      ],
      "owner": {
        "reputation": 436932,
        "user_id": 87234,
        "user_type": "registered",
        "accept_rate": 100,
        "profile_image": "https://i.stack.imgur.com/FkjBe.png?s=128&g=1",
        "display_name": "GManNickG",
        "link": "https://stackoverflow.com/users/87234/gmannickg"
      },
      "is_answered": true,
      "view_count": 795834,
      "protected_date": 1297320482,
      "accepted_answer_id": 1642035,
      "answer_count": 23,
      "community_owned_date": 1262233068,
      "score": 8938,
      "last_activity_date": 1591930965,
      "creation_date": 1256799465,
      "last_edit_date": 1584387179,
      "question_id": 1642028,
      "content_license": "CC BY-SA 4.0",
      "link": "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
      "title": "What is the &quot;--&gt;&quot; operator in C++?"
    }
}
```

# Stop Scraper
>> Not implemented

**URL** : `/scrape/stop`

**Method** : `POST`

# Mongo API
# Mongo Home
**URL** : `/mongo`

**Method** : `GET`

## Success Response
**Code**: `200 OK`

**Content Example**
```HTML
<p> Mongo Home </p>
```

# Start Scraper
**URL** : `/mongo/test`

**Method** : `GET`

## Success Response
**Code**: `200 OK`

**Content Example**
```json
{
        "_id": {
            "$oid": "5ef2f347f2f11a6a2efddfba"
        },
        "url": "https://stackoverflow.com/questions/1712227/how-do-i-get-the-number-of-elements-in-a-list",
        "title": "python - How do I get the number of elements in a list? - Stack Overflow",
        "code": [
            "items = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n\n# FAKE METHOD:\nitems.amount()  # Should return 3\n",
            "items",
            "len()",
            ">>> len([1,2,3])\n3\n",
            "len()",
            "len()",
            "len",
            "items = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n",
            "len(items)\n",
            "ob_size",
            "len",
            "len(s)",
            "len",
            "__len__",
            "object.__len__(self)",
            "len()",
            "__nonzero__()",
            "__bool__()",
            "__len__()",
            "__len__",
            "items.__len__()\n",
            "len",
            ">>> all(hasattr(cls, '__len__') for cls in (str, bytes, tuple, list, \n                                            xrange, dict, set, frozenset))\nTrue\n",
            "len",
            "if len(items) == required_length:\n    ...\n",
            "if len(items): \n    ...\n",
            "if items:     # Then we have some items, not empty!\n    ...\n",
            "if not items: # Then we have an empty list!\n    ...\n",
            "if items",
            "if not items",
            "length",
            "class slist(list):\n    @property\n    def length(self):\n        return len(self)\n",
            ">>> l = slist(range(10))\n>>> l.length\n10\n>>> print l\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n",
            "length",
            "length = property(len)",
            "len",
            "len",
            "operator.length_hint",
            "list",
            "length_hint",
            ">>> from operator import length_hint\n>>> l = [\"apple\", \"orange\", \"banana\"]\n>>> len(l)\n3\n>>> length_hint(l)\n3\n\n>>> list_iterator = iter(l)\n>>> len(list_iterator)\nTypeError: object of type 'list_iterator' has no len()\n>>> length_hint(list_iterator)\n3\n",
            "length_hint",
            "len",
            "__len__",
            "list",
            "len",
            "length_hint",
            "sys.maxsize",
            "len",
            "__len__",
            "items = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n\nprint items.__len__()\n",
            "__foo__",
            "_foo",
            "__foo",
            "_classname__foo",
            "len()",
            "def count(list):\n    item_count = 0\n    for item in list[:]:\n        item_count += 1\n    return item_count\n\ncount([1,2,3,4,5])\n",
            "list[:]",
            "connect(int sockfd, const struct sockaddr *addr, socklen_t addrlen);",
            "class MyList(object):\n    def __init__(self):\n        self._data = []\n        self.length = 0 # length tracker that takes up memory but makes length op O(1) time\n\n\n        # the implicit iterator in a list class\n    def __iter__(self):\n        for elem in self._data:\n            yield elem\n\n    def add(self, elem):\n        self._data.append(elem)\n        self.length += 1\n\n    def remove(self, elem):\n        self._data.remove(elem)\n        self.length -= 1\n\nmylist = MyList()\nmylist.add(1)\nmylist.add(2)\nmylist.add(3)\nprint(mylist.length) # 3\nmylist.remove(3)\nprint(mylist.length) # 2\n",
            "for item in list[:]:",
            "for item in list:",
            "+= 1",
            "len()",
            "static PyObject *\nbuiltin_len(PyObject *module, PyObject *obj)\n/*[clinic end generated code: output=fa7a270d314dfb6c input=bc55598da9e9c9b5]*/\n{\n    Py_ssize_t res;\n\n    res = PyObject_Size(obj);\n    if (res < 0) {\n        assert(PyErr_Occurred());\n        return NULL;\n    }\n    return PyLong_FromSsize_t(res);\n}\n",
            "Py_ssize_t",
            "PyObject_Size()",
            "if (res < 0) {\n        assert(PyErr_Occurred());\n        return NULL;\n    }\n",
            "return PyLong_FromSsize_t(res);\n",
            "res",
            "C",
            "long",
            "longs"
        ],
        "text": [
            "\nConsider the following:\nitems = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n\n# FAKE METHOD:\nitems.amount()  # Should return 3\n\nHow do I get the number of elements in the list items?\n",
            "\nThe len() function can be used with several different types in Python - both built-in types and library types. For example:\n>>> len([1,2,3])\n3\n\nOfficial 2.x documentation is here: len()\nOfficial 3.x documentation is here: len()\n",
            "\n\nHow to get the size of a list?\n\nTo find the size of a list, use the builtin function, len:\nitems = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n\nAnd now:\nlen(items)\n\nreturns 3.\nExplanation\nEverything in Python is an object, including lists. All objects have a header of some sort in the C implementation. \nLists and other similar builtin objects with a \"size\" in Python, in particular, have an attribute called ob_size, where the number of elements in the object is cached. So checking the number of objects in a list is very fast.\nBut if you're checking if list size is zero or not, don't use len - instead, put the list in a boolean context - it treated as False if empty, True otherwise.\nFrom the docs\nlen(s)\n\nReturn the length (the number of items) of an object. The argument may be a sequence (such as a string, bytes, tuple, list, or range) or\n  a collection (such as a dictionary, set, or frozen set).\n\nlen is implemented with __len__, from the data model docs:\nobject.__len__(self)\n\nCalled to implement the built-in function len(). Should return the length of the object, an integer >= 0. Also, an object that doesn’t\n  define a __nonzero__() [in Python 2 or __bool__() in Python 3] method and whose __len__() method returns zero\n  is considered to be false in a Boolean context.\n\nAnd we can also see that __len__ is a method of lists:\nitems.__len__()\n\nreturns 3.\nBuiltin types you can get the len (length) of\nAnd in fact we see we can get this information for all of the described types:\n>>> all(hasattr(cls, '__len__') for cls in (str, bytes, tuple, list, \n                                            xrange, dict, set, frozenset))\nTrue\n\nDo not use len to test for an empty or nonempty list\nTo test for a specific length, of course, simply test for equality:\nif len(items) == required_length:\n    ...\n\nBut there's a special case for testing for a zero length list or the inverse. In that case, do not test for equality.\nAlso, do not do:\nif len(items): \n    ...\n\nInstead, simply do:\nif items:     # Then we have some items, not empty!\n    ...\n\nor\nif not items: # Then we have an empty list!\n    ...\n\nI explain why here but in short, if items or if not items is both more readable and more performant.\n",
            "\nWhile this may not be useful due to the fact that it'd make a lot more sense as being \"out of the box\" functionality, a fairly simple hack would be to build a class with a length property:\nclass slist(list):\n    @property\n    def length(self):\n        return len(self)\n\nYou can use it like so:\n>>> l = slist(range(10))\n>>> l.length\n10\n>>> print l\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n\nEssentially, it's exactly identical to a list object, with the added benefit of having an OOP-friendly length property.\nAs always, your mileage may vary.\n",
            "\nBesides len you can also use operator.length_hint (requires Python 3.4+). For a normal list both are equivalent, but length_hint makes it possible to get the length of a list-iterator, which could be useful in certain circumstances:\n>>> from operator import length_hint\n>>> l = [\"apple\", \"orange\", \"banana\"]\n>>> len(l)\n3\n>>> length_hint(l)\n3\n\n>>> list_iterator = iter(l)\n>>> len(list_iterator)\nTypeError: object of type 'list_iterator' has no len()\n>>> length_hint(list_iterator)\n3\n\nBut length_hint is by definition only a \"hint\", so most of the time len is better.\nI've seen several answers suggesting accessing __len__. This is all right when dealing with built-in classes like list, but it could lead to problems with custom classes, because len (and length_hint) implement some safety checks. For example, both do not allow negative lengths or lengths that exceed a certain value (the sys.maxsize value). So it's always safer to use the len function instead of the __len__ method!\n",
            "\nAnswering your question as the examples also given previously:\nitems = []\nitems.append(\"apple\")\nitems.append(\"orange\")\nitems.append(\"banana\")\n\nprint items.__len__()\n\n",
            "\nAnd for completeness (primarily educational), it is possible without using the len() function. I would not condone this as a good option DO NOT PROGRAM LIKE THIS IN PYTHON, but it serves a purpose for learning algorithms.\ndef count(list):\n    item_count = 0\n    for item in list[:]:\n        item_count += 1\n    return item_count\n\ncount([1,2,3,4,5])\n\n(The colon in list[:] is implicit and is therefore also optional.)\nThe lesson here for new programmers is: You can’t get the number of items in a list without counting them at some point. The question becomes: when is a good time to count them? For example, high-performance code like the connect system call for sockets (written in C) connect(int sockfd, const struct sockaddr *addr, socklen_t addrlen);, does not calculate the length of elements (giving that responsibility to the calling code). Notice that the length of the address is passed along to save the step of counting the length first? Another option: computationally, it might make sense to keep track of the number of items as you add them within the object that you pass. Mind that this takes up more space in memory. See Naftuli Kay‘s answer.\nExample of keeping track of the length to improve performance while taking up more space in memory. Note that I never use the len() function because the length is tracked:\nclass MyList(object):\n    def __init__(self):\n        self._data = []\n        self.length = 0 # length tracker that takes up memory but makes length op O(1) time\n\n\n        # the implicit iterator in a list class\n    def __iter__(self):\n        for elem in self._data:\n            yield elem\n\n    def add(self, elem):\n        self._data.append(elem)\n        self.length += 1\n\n    def remove(self, elem):\n        self._data.remove(elem)\n        self.length -= 1\n\nmylist = MyList()\nmylist.add(1)\nmylist.add(2)\nmylist.add(3)\nprint(mylist.length) # 3\nmylist.remove(3)\nprint(mylist.length) # 2\n\n",
            "\nIn terms of how len() actually works, this is its C implementation:\nstatic PyObject *\nbuiltin_len(PyObject *module, PyObject *obj)\n/*[clinic end generated code: output=fa7a270d314dfb6c input=bc55598da9e9c9b5]*/\n{\n    Py_ssize_t res;\n\n    res = PyObject_Size(obj);\n    if (res < 0) {\n        assert(PyErr_Occurred());\n        return NULL;\n    }\n    return PyLong_FromSsize_t(res);\n}\n\nPy_ssize_t is the maximum length that the object can have. PyObject_Size() is a function that returns the size of an object. If it cannot determine the size of an object, it returns -1. In that case, this code block will be executed:\nif (res < 0) {\n        assert(PyErr_Occurred());\n        return NULL;\n    }\n\nAnd an exception is raised as a result. Otherwise, this code block will be executed:\nreturn PyLong_FromSsize_t(res);\n\nres which is a C integer, is converted into a python long and returned. All python integers are stored as longs since Python 3.\n"
        ],
        "tags": [
            "python",
            "list"
        ]
},...
```

# Add to DB
**URL** : `/mongo/test`

**Method** : `POST`

**Data Constraints** : Mongo has no requirements, but keep to the above format

## Success Response
**Code**: `200 OK`


# Delete all entries in collection
**URL** : `/mongo/test`

**Method** : `DELETE`

## Success Response
**Code**: `200 OK`
