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
