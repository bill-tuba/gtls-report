(ns badams.api-test
  (:require [badams.api :as sut]
            [badams.repository :as repo]
            [clojure.data.json :as json]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(def status+body
  (juxt :status (comp #(json/read-str % :key-fn keyword) :body)))

(deftest api-test
  (testing "Not found"
    (let [handler  (sut/app {:components/repo (atom [])})
          response (handler (mock/request :get "/TILT!"))]
      (is (= 404 (:status response)))))

  (testing "GET returns no items with empty state"
    (are [request expected]
         (let [repo     (repo/atomic-repo)
               handler  (sut/app {:components/repo (repo/atomic-repo)})
               response (handler (mock/request :get request))]
           (is (= expected (status+body response))))

      "/records/name"      [200 []]
      "/records/birthdate" [200 []]
      "/records/email"     [200 []]))

  (testing "POST then GET records"
    (are [post-req get-url expected]
         (let [[url payload] post-req
               repo      (repo/atomic-repo)
               handler   (sut/app {:components/repo (atom [])})
               post-resp (handler (-> (mock/request :post url)
                                      (mock/body    payload)))
               get-resp  (handler (mock/request :get get-url))]

           (and (is (= 201      (:status post-resp)))
                (is (= expected (status+body get-resp)))))

      ["/records/" "a,b,c,d,1/2/1970"]
      "/records/name"
      [200 [{:LastName      "a",
             :FirstName     "b",
             :Email         "c",
             :FavoriteColor "d",
             :DateOfBirth   "1/2/1970"}]])))
