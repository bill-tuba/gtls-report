(ns badams.cli
  (:gen-class)
  (:require [badams.common :as common]
            [badams.core :as core]
            [badams.repository :as repo]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.walk :as walk]))

(defn create-repo! [resource-path]
  (with-open [rdr (io/reader resource-path)]
    (let [repo (repo/atomic-repo)]
      (doseq [line (line-seq rdr)]
        (some->> (core/parse line)
                 (repo/put!  repo)))
      repo)))

(def report-views
  {repo/by-email-desc-last-name-asc "By email descending, last name ascending"
   repo/by-birth-date-asc           "By birth date ascending"
   repo/by-last-name-desc           "By last name descending"})

(defn ^:private show-table! [title coll]
  (println (str/join "" (repeat (count title) " ")))
  (printf  "\n|\t%s:\n|" title)
  (pp/print-table (walk/stringify-keys coll)))

(defn show-report! [repo [order title :as view]]
  (->> (repo/values repo {:order order})
       (map core/prepare)
       (show-table! title)))

(defn -main [& args]
  (let [file (get (common/options args)  "-f")
        repo (create-repo! file)]
    (doseq [v report-views]
      (show-report! repo v))))
