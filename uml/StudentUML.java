+-----------------------------------------------------------------------+
|                                 Student                               |
+-----------------------------------------------------------------------+
| - year                    : int                                       |
| - major                   : String                                    |
| - submittedApplicationIDs : List<String>                              |
| - acceptedInternship      : String                                    |
+-----------------------------------------------------------------------+
| + Student(userID: String,                                            |
|           name: String,                                              |
|           email: String,                                             |
|           pwd: String,                                               |
|           year: int,                                                 |
|           major: String)                                             |
|                                                                       |
| + getYear()                    : int                                  |
| + getMajor()                   : String                               |
| + getSubmittedApplicationIDs() : List<String>                         |
| + getAcceptedInternshipID()    : String                               |
| + hasAcceptedInternship()      : boolean                              |
| + getStudentID()               : String                               |
|                                                                       |
| + setYear(y: int)              : void                                 |
| + setMajor(m: String)          : void                                 |
| + setAcceptedPlacement(appID: String) : void                          |
|                                                                       |
| + addApplication(appID: String)    : boolean                          |
| + removeApplication(appID: String) : boolean                          |
+-----------------------------------------------------------------------+

<<uses>>
- User
- List
- ArrayList
