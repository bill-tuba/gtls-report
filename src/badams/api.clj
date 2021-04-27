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

(defn add-details-handler [{:keys [components/repo body] :as request}]
  (->> (core/parse (slurp body))
       (repo/put! repo))
  (res/response {:status 201}))

(def not-found (constantly (res/response {:status 404})))

(defn sorting-handler [order]
  (fn [{:keys [components/repo] :as request}]
    (res/response (repo/values repo {:order order}))))

(def handler
  (make-handler
    ["/" [["records/"
           [[:post [["" add-details-handler]]]
            [:get  [["email"     (sorting-handler repo/by-email-desc-last-name-asc)]
                    ["birthdate" (sorting-handler repo/by-birth-date-asc)]
                    ["name"      (sorting-handler repo/by-last-name-desc)]]]]]]]))

(defonce ^:private server
  (atom nil))

(defn with-components [components handler]
  (fn [request]
    (handler (merge request components))))

(defn app [components]
  (->> #'handler
       (with-components components)
       wrap-json-response
       wrap-reload))

(defn start-server [port repo]
  (reset! server
          (server/run-server (#'app {:components/repo repo})
                             {:port port})))
(defn stop-server []
  (@server :timeout 100))

(defn -main [& args]
  (let [port (-> (common/options args)
                 (get  "-p" "8000")
                 (#(Integer/parseInt %)))
        repo (repo/atomic-repo)]

    (start-server port repo)
    (printf "API listening on port: %s\n" port)
    (println "\nCtrl-C to exit ....")))
