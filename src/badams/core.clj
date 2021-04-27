(ns badams.core
  (:require [clojure.string :as str]))

(def ^:private DateFormat "M/d/yyyy")

(defn date-formatter
  ([] (date-formatter DateFormat))
  ([fmt]
   (java.text.SimpleDateFormat. fmt)))

(defn date
  ([date-str] (date DateFormat date-str))
  ([fmt date-str]
   (.parse (date-formatter) date-str)))

(def Transformations
  [[:LastName      identity]
   [:FirstName     identity]
   [:Email         identity]
   [:FavoriteColor identity]
   [:DateOfBirth   (partial date DateFormat)]])

(defn parse
  ([line] (parse Transformations line))
  ([t-formations line]
   (letfn [(detail [[k f] v] {k (f v)})]
     (when-not (str/blank? line)
       (->> (str/split line #"[|,\s]")
            (map detail t-formations)
            (into {}))))))

(defn prepare [details]
  (letfn [(format-date [d] (.format (date-formatter) d))]
    (update details :DateOfBirth format-date)))
