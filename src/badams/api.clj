(ns badams.api
  (:gen-class)
  (:require [badams.common :as common]
            [badams.core :as core]
            [badams.repository :as repo]
            [bidi.ring :refer [make-handler]]
            [org.httpkit.server :as server]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :as res]))

(defn add-to-repo-handler [{:keys [components/repo body] :as request}]
  (->> (core/parse (slurp body))
       (repo/put! repo))
  (res/created "/records/unsorted"))

(def ^:private not-found
  (constantly (res/not-found "Not Found")))

(defn- sorting-handler [order]
  (fn [{:keys [components/repo] :as request}]
    (->> (repo/values repo {:order order})
         (map core/prepare)
         res/response)))

(def ^:private handler
  (make-handler
    ["/" [
          ["records/"
           [[:post [["" add-to-repo-handler]]]
            [:get  [["email"     (sorting-handler repo/by-email-desc-last-name-asc)]
                    ["birthdate" (sorting-handler repo/by-birth-date-asc)]
                    ["name"      (sorting-handler repo/by-last-name-desc)]

                    ;; POST-ing should have 'location'. ergo this
                    ["unsorted"  (sorting-handler repo/unsorted)]]]]]
          [true not-found]]]))

(defn- with-components [component-map handler]
  (fn [request]
    (handler (merge request component-map))))

(defn app [components]
  (->> #'handler
       (with-components components)
       wrap-json-response
       wrap-reload))

(defonce ^:private server
  (atom nil))

(defn- start-server [port repo]
  (reset! server
          (server/run-server (#'app {:components/repo repo})
                             {:port port})))
(defn- stop-server []
  (@server :timeout 100))

(defn -main [& args]
  (let [port (-> (common/options args)
                 (get  "-p" "8081")
                 (#(Integer/parseInt %)))
        repo (repo/atomic-repo)]

    (start-server port repo)
    (printf "API listening on port: %s\n" port)
    (println "\nCtrl-C to exit ....")))
