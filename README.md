How to run :  
`docker image build -t categorizer:latest .`

`docker run -p 8080:8080 categorizer:latest`

Request sample.

    POST http://localhost:8080/api/vi/categorize
    {
        "categories": [
            "Star Wars",
            "Basketball"
            ],
        "urls":[
            "http://www.starwars.com",
            "https://www.imdb.com/find?q=star+wars&ref_=nv_sr_sm",
            "https://edition.cnn.com/sport"
            ]
    }

Response sample.
 
    {
    "https://www.imdb.com/find?q=star+wars&ref_=nv_sr_sm": [
        "Star Wars"
    ],
    "https://edition.cnn.com/sport": [
        "Basketball"
    ],
    "http://www.starwars.com": [
        "Star Wars"
    ]
    }
