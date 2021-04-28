(ns badams.cli
  (:gen-class)
  (:require [badams.details :as core]
            [badams.repository :as repo]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.walk :as walk]))

(defn populate-repo! [files]
  (let [repo (repo/atomic-repo)]
    (doseq [f files]
      (with-open [rdr (io/reader f)]
        (doseq [line (line-seq rdr)]
          (some->> (core/parse line)
                   (repo/put!  repo)))))
    repo))

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
       (map core/format-details)
       (show-table! title)))

(defn assert-exists! [files]
  (assert (every? #(.exists (io/file %)) files)
          (format "at least one file:\n %s \n does not exist\n" files)))

(defn usage []
  (println "usage: badams.cli\noptions:\n\t paths to csv files"))

(defn error [t]
  (binding [*out* *err*]
    (usage)
    (println "\n" (.getMessage t))
    (System/exit 1)))

(defn -main [& args]
  (try
    (let [files args
          _    (assert-exists! files)
          repo (populate-repo! files)]
      (doseq [v report-views]
        (show-report! repo v)))
    (catch Throwable t
      (error t))))
