(ns badams.details
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]))

(s/def ::LastName      (s/and string? not-empty))
(s/def ::FirstName     (s/and string? not-empty))
(s/def ::Email         (s/and string? not-empty))
(s/def ::FavoriteColor #{"RED" "ORANGE" "YELLOW"
                         "GREEN" "BLUE" "INDIGO" "VIOLET"})
(s/def ::DateOfBirth   inst?)

(s/def ::details
  (s/keys :req-un
          [::LastName
           ::FirstName
           ::Email
           ::FavoriteColor
           ::DateOfBirth]))

(def fields (map (comp keyword name) (last (s/describe ::details))))

(def ^:private DateFormat "M/d/yyyy")

(defn date-formatter
  ([] (date-formatter DateFormat))
  ([fmt]
   (java.text.SimpleDateFormat. fmt)))

(defn date
  ([date-str] (date DateFormat date-str))
  ([fmt date-str]
   (.parse (date-formatter) date-str)))

(defn- conform-details [m]
  (update m :DateOfBirth date))

(defn- validate [schema m]
  (when (s/valid? schema m)
    m))

(defn parse [line]
  (try
    (->> (str/split line #"[|,\s]")
         (zipmap fields)
         (conform-details)
         (validate ::details))
    (catch Throwable _ nil)))

(defn format-details [details]
  (letfn [(format-date [d] (.format (date-formatter) d))]
    (update details :DateOfBirth format-date)))
