# gdata-books-clojure

Clojure friendly wrapper for Google's [GData Books API][overview]. ([javadocs][javadocs])

## Usage

If you are using [leiningen][leiningen], add gdata-books-clojure to your dependencies:

   [gdata-books-clojure "1.0.0-SNAPSHOT"]

First, tell the library your name, your application's name, and version number, 
in "yourname-appname-1.0" format:

       (set-application-name "joeking-kewlapp-1.0")

Google uses this information to identify applications that are abusing the API.

Then you can search for books by title, or title and author:

     user> (search "Stranger in a Strange Land")
     #<VolumeFeed {VolumeFeed com.google.gdata.data.books.VolumeFeed@1d95643}>

     user> (search {:title "Stranger in a Strange Land", :author "Robert Heinlein"})
     #<VolumeFeed {VolumeFeed com.google.gdata.data.books.VolumeFeed@578b06}>

     user> (search {:title "Stranger in a Strange Land", :author "Heather L. Katz"})
     #<VolumeFeed {VolumeFeed com.google.gdata.data.books.VolumeFeed@13f54ae}>

search returns a VolumeFeed which represents an Atom feed for search results for 
the book. You can use the helper functions in gdata-books-clojure to tease out 
the pieces of information you want, without having to know the structure of the 
atom feed or the the java API:

    user> (map creators (entries (search "Stranger in a Strange Land")))
    (("Robert A. Heinlein") ("Gary Younge") ("Robert Anson Heinlein") ("Heather L. Katz") ...)

    user> (map titles (entries (search {:title "Stranger in a Strange Land", :author "Heather L. Katz"})))
    (("Stranger in a strange land" "Muslim political identity in the context of liberal democracy"))

    user> (map titles (entries (search {:author "H Beam Piper"})))
    (("Little Fuzzy") ("Space Viking") ("Uller Uprising") ("Time Crime") ("The Cosmic Computer") ...)

Using these building blocks, you could build really complicated queries:

      ;; Search for books with "Monkeys" then search for all books by the
      ;; authors of the monkey books
      (->> (search "Monkeys")
           entries
           (map creators)
           flatten
           distinct
           (map #(search {:author %})))

## TODO

* Only the first 10 results are returned in the feed from search. I was thinking 
  it might be fun to use some kind of lazy computation to hide the fact that the 
  feeds are paged.
* I haven't really used much of the API for what I am trying to do. Some of this
  might not actually work the way I expect.
* I am still a little unsure of the right way to test stuff like this in
  clojure: in static languages, I would try to do to stub out the Google, but 
  the tests I have seem to work OK when you have access to the internet, so...

## License

Copyright (C) 2010 Chris Bilson

Distributed under the Eclipse Public License, the same as Clojure.

[overview]: http://code.google.com/apis/books/docs/gdata/developers_guide_java.html
[javadocs]: http://code.google.com/apis/gdata/javadoc/
