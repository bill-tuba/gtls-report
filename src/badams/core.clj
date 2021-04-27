(ns badams.core
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def ^:private DateFormat "M/d/yyyy")

(defn date-formatter
  ([] (date-formatter DateFormat))
  ([fmt]
   (java.text.SimpleDateFormat. fmt)))

(defn date
  ([date-str] (date DateFormat date-str))
  ([fmt date-str]
   (.parse (date-formatter) date-str)))

(def RequiredFields
  {:LastName      str
   :FirstName     str
   :Email         str
   :FavoriteColor str
   :DateOfBirth   (partial date DateFormat)})

(defn- assert-required [schema m]
  (assert (empty? (set/difference (set (keys schema))
                                  (set (keys m)))))
  m)

(defn parse
  ([line] (parse RequiredFields line))
  ([schema line]
   (letfn [(detail [[k f] v] {k (f v)})]
     (when-not (str/blank? line)
       (try
         (->> (str/split line #"[|,\s]")
              (map detail schema)
              (into {})
              (assert-required schema))
         (catch Throwable _ nil))))))

(defn prepare [details]
  (letfn [(format-date [d] (.format (date-formatter) d))]
    (update details :DateOfBirth format-date)))
