(ns gdata-books-clojure.core
  (:use [clojure.contrib.string :only [join split replace-re]])
  (:import (com.google.gdata.client.books VolumeQuery BooksService)
           (com.google.gdata.data.books VolumeFeed)
           (java.net URL)))

(def settings (atom {:application-name "unknown"
                     :base-url "http://www.google.com/books/feeds/volumes"}))

(defn set-application-name [name]
  (swap! settings assoc :application-name name))

(defn application-name []
  (:application-name @settings))

(defn- url [s] (URL. s))

(defn- in-author [s]
  (str "inauthor:" s))

(defn- build-author-operators
  "Given an author name, like \"Joe O'Brien King\"
   returns a string like \"inauthor:Joe inauthor:O'Brien inauthor:King\"
   so we can use google book search advanced operators to
   get books by a specific author."
  [author]
  (let [tokens (split #"\s+" author)
        name-tokens (map #(replace-re #"[^A-Za-z'-]" "" %) tokens)
        long-name-tokens (filter #(< 1 (.length %)) name-tokens)
        author-operators (map in-author long-name-tokens)]
    (if (empty? author-operators)
      nil
      (join " " author-operators))))

(defn- create-query
  "Creates a google books query for a title and optional author"
  [{title :title, author :author}]
  (let [query (VolumeQuery. (url (:base-url @settings)))
        author-operators (if author (build-author-operators author))
        query-text (if (empty? author-operators)
                     title
                     (join " " [title author-operators]))]
    (.setFullTextQuery query query-text)
    query))

(defn- do-query
  "Executes a google books query"
  [query]
  (let [service (BooksService. (:application-name @settings))]
    (.query service query VolumeFeed)))

(defn search
  "Searches for a book using google books. Can be called like:
   (seach \"Stranger in a strange land\")
   -or-
   (search {:title \"Stranger in a strange land\", :author \"Robert Heinlein\"})"
  [args]
  (let [criteria (if (map? args) args {:title args})
        query (create-query criteria)]
    (do-query query)))

(defn etag
  "Get the E-Tag from a link"
  [link]
  (.getEtag link))

(defn href
  "Get the href from a link"
  [link]
  (.getHref link))

(defn rel
  "Get the rel (relationship) from a link"
  [link]
  (.getRel link))

(defn text
  "Get the text from some element x"
  [x]
  (.getText x))

(defn title
  "Get the title of some element x"
  [x]
  (.getTitle x))

(defn link-type
  "Get the link type from a link"
  [link]
  (.getType link))

(defn value
  "Get the value of some element x"
  [x]
  (.getValue x))

(defn values
  "Get all the values from some elements"
  [xs]
  (map value xs))

(defn alternate-links
  "Get the atom alternate links from an entry"
  [x]
  (.getAtomAlternateLinks x))

(defn comments?
  "Check to see if an entry has comments"
  [entry]
  (.hasComments entry))

(defn comments
  "Get the comments from an entry"
  [entry]
  (.getComments entry))

(defn creators?
  "Check to see if an entry has any creators"
  [entry]
  (.hasCreators entry))

(defn creators
  "Gets the creators for an entry"
  [entry]
  (values (.getCreators entry)))

(defn descriptions?
  "Checks to see if an entry has any descriptions"
  [entry]
  (.hasDescriptions entry))

(defn descriptions
  "Gets the descriptions for an entry"
  [entry]
  (values (.getDescriptions entry)))

(defn entries
  "Gets the entries in a feed"
  [feed]
  (.getEntries feed))

(defn identifiers?
  "Checks to see if an entry has any identifiers"
  [entry]
  (.hasIdentifiers entry))

(defn identifiers
  "Gets all the identifiers for an entry"
  [entry]
  (values (.getIdentifiers entry)))

(defn languages?
  "Checks to see if an entry has any languages"
  [entry]
  (.hasLanguages entry))

(defn languages
  "Gets all the languages for an entry"
  [entry]
  (values (.getLanguages entry)))

(defn publishers?
  "Checks to see if an entry has any publishers"
  [entry]
  (.hasPublishers entry))

(defn publishers
  "Gets all the publishers for an entry"
  [entry]
  (values (.getPublishers entry)))

(defn rating?
  "Checks to see if an entry has any rating"
  [entry]
  (.hasRating entry))

(defn rating
  "Gets the rating for an entry"
  [entry]
  (value (.getRating entry)))

(defn review?
  "Checks to see if an entry has any review"
  [entry]
  (.hasReview entry))

(defn review
  "Gets the review for an entry"
  [entry]
  (.getReview entry))

(defn subjects?
  "Checks to see if an entry has any subjects"
  [entry]
  (.hasSubjects entry))

(defn subjects
  "Gets all the subjects for an entry"
  [entry]
  (values (.getSubjects entry)))

(defn titles?
  "Checks to see if an entry has any titles"
  [entry]
  (.hasTitles entry))

(defn titles
  "Gets all the titles for an entry"
  [entry]
  (values (.getTitles entry)))

(defn thumbnail
  "Gets the thumbnail link for an entry"
  [entry]
  (href (.getThumbnailLink entry)))
