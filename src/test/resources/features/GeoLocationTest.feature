Feature: Geolocation!
  blah, blah, blah

  Scenario Outline: Check a test case for 07440
    Given I have an account at http://api.geonames.org of "howarddeiner", but I am testing on a fake
	 When I find nearby postal codes with postalCode "07440", country "US", and radius "10.0"
	 Then for row = "<row>", placeName = "<placeName>", postalCode = "<postalCode>", latitude = "<latitude>", longitude = "<longitude>", and distance = "<distance>"
	  
	Examples:
      |row|placeName     |postalCode|latitude |longitude |distance|
      |0  |Pequannock    |07440     |40.947308|-74.29601 |0.0     |
      |1  |Pompton Plains|07444     |40.965515|-74.301602|2.07817 |
      |2  |Lincoln Park  |07035     |40.920769|-74.299512|2.96549 |
      |3  |Wayne         |07470     |40.947112|-74.246565|4.1526  |
      |4  |Towaco        |07082     |40.927691|-74.342807|4.49536 |
        
  Scenario Outline: Check a test case for 60008
    Given I have an account at http://api.geonames.org of "howarddeiner"
	 When I find nearby postal codes with postalCode "60008", country "US", and radius "10.0"
	 Then for row = "<row>", placeName = "<placeName>", postalCode = "<postalCode>", latitude = "<latitude>", longitude = "<longitude>", and distance = "<distance>"
	  
	Examples:
      |row|placeName        |postalCode|latitude|longitude|distance|
      |0  |Rolling Meadows  |60008     |42.07   |-88.02   |0.0     |
      |1  |Palatine         |60038     |42.10   |-88.01   |2.8     |
      |2  |Palatine         |60055     |42.10   |-88.01   |2.8     |
      |3  |Schaumburg       |60173     |42.06   |-88.05   |2.9     |
      |4  |Arlington Heights|60005     |42.06   |-87.99   |2.9     |