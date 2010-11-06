(ns gdata-books-clojure.test.core
  (:use [gdata-books-clojure.core] :reload)
  (:use [clojure.test]))

(deftest changing-application-name
  (let [old-name application-name]
    (set-application-name "monkey-banana-1.0")
    (is (not= old-name (application-name)))
    (is (= "monkey-banana-1.0" (application-name)))))

(defn expect-stranger-in-a-strange-land [book]
  (is (not (comments? book)))
  
  (is (creators? book))
  (is (= 1 (count (creators book))))
  (is (= '("Robert A. Heinlein") (creators book)))
  
  (is (descriptions? book))
  (is (= 1 (count (descriptions book))))
  (is (= '("This is the epic saga of an earthling, Valentine Michael Smith, born and educated onMars, who arrives on our planet with &quot;psi&quot; powers--telepathy, clairvoyance, ...")
         (descriptions book)))
  
  (is (identifiers? book))
  (is (= 3 (count (identifiers book))))
  (is (= '("Kn1bNU91sAoC" "ISBN:0441790348" "ISBN:9780441790340") (identifiers book)))
  
  (is (not (languages? book)))
  
  (is (publishers? book))
  (is (= 1 (count (publishers book))))
  (is (= '("ACE Charter") (publishers book)))
  
  (is (rating? book))
  (is (= nil (rating book)))
  
  (is (not (review? book)))
  
  (is (subjects? book))
  (is (= 1 (count (subjects book))))
  (is (= '("Fiction") (subjects book)))
  
  (is (titles? book))
  (is (= 1 (count (titles book))))
  (is (= '("Stranger in a strange land") (titles book)))
  
  (is (= "http://bks2.books.google.com/books?id=Kn1bNU91sAoC&printsec=frontcover&img=1&zoom=5&sig=ACfU3U0KLwNG0JlVVQSeUZ6SV-zuBSCyFg&source=gbs_gdata"
         (thumbnail book))))

(deftest finding-books-by-title-only
  (let [result (search "Stranger in a Strange Land")]
    (is (= 10 (count (entries result))))
    (expect-stranger-in-a-strange-land (first (entries result)))))

(deftest finding-books-by-title-and-author
  (let [result (search {:title "Stranger in a Strange Land" :author "Robert Heinlein"})]
    (is (= 10 (count (entries result))))
    (expect-stranger-in-a-strange-land (first (entries result)))))

