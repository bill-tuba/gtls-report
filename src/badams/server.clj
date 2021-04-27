(ns badams.server
  (:gen-class)
  (:require [badams.api :as api]
            [badams.common :as common]
            [badams.repository :as repo]
            [org.httpkit.server :as server]))

(defonce ^:private -server
  (atom nil))

(defn- start-server [port repo]
  (reset! -server
          (server/run-server (#'api/app {:components/repo repo})
                             {:port port})))
(defn- stop-server []
  (@-server :timeout 100))

(defn- usage []
  (println "usage: badams.server\noptions:\n\t-p local port number"))

(defn error [t]
  (binding [*out* *err*]
    (usage)
    (println "\n" (.getMessage t))
    (System/exit 1)))

(defn -main
  "Main Entry point for the REST (like) api server"
  [& args]
  (try
    (let [port (-> (common/options args)
                   (get  "-p" "8081")
                   (#(Integer/parseInt %)))
          repo (repo/atomic-repo)]

      (start-server port repo)
      (printf "API listening on port: %s\n" port)
      (println "\nCtrl-C to exit ...."))
    (catch Throwable t
      (error t))))
