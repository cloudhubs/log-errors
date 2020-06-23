# API

## scraper
> /scrape
>
Welcome page
 > /scrape/{language}
>
begin a scraper on the given language `('python', 'csharp', 'java')`
> /scrape-meta/
>
scrapes only the first page of the parent links
>/stop/scrape
>
Stop scraper
`not currently implemented`

## mongo
> /mongo
>
   Welcome page
> /mongo/test/add
>
json (from body) to collection `testdb.coll_name`
> /mongo/test/find
>
Returns all documents in `testdb.coll_name`
> /mongo/test/empty
>
empties `testdb.coll_name`

