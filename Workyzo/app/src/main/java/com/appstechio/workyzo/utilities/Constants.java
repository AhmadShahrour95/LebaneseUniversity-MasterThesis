package com.appstechio.workyzo.utilities;

import android.os.Bundle;

import com.appstechio.workyzo.R;
import com.google.common.collect.Multimap;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    //Users
    public static final  String KEY_COLLECTION_USERS = "Users";
    public static final  String KEY_USERID = "UserId";
    public static final  String KEY_FIRSTNAME = "First_name";
    public static final  String KEY_LASTNAME = "Last_name";
    public static final  String KEY_USERNAME = "Username";
    public static final  String KEY_GENDER = "Gender";
    public static final  String KEY_SALARY = "Salary";
    public static final  String KEY_SALARY_TYPE = "Type";
    public static final  String KEY_SALARY_AMOUNT = "Amount";
    public static final  String KEY_PROFESSIONAL_HEADLINE = "Professional_headline";
    public static final  String KEY_USER_SUMMARY = "User_summary";
    public static final  String KEY_EMAILADDRESS = "Email_address";
    public static final  String KEY_PASSWORD = "Password";
    public static final  String KEY_MOBILE_NUMBER = "Mobile_number";
    public static final  String KEY_COUNTRY = "Country";
    public static final  String KEY_CITY = "City";
    public static final  String KEY_LOCATION = "Location";
    public static final  String KEY_COUNTRY_FLAG = "Country_flag";
    public static final  String KEY_COUNTRY_CODE = "Country_Code";
    public static final  String KEY_ADDRESS = "Address";
    public static final  String KEY_COMPLETED= "Completed";
    public static final  String KEY_COMPLETED_JOBS= "Completed_jobs";
    public static final  String KEY_HIRED_JOBS= "Hired_jobs";
    public static final  String KEY_POSTED_JOBS = "Posted_jobs";
    public static final  String KEY_APPLIED_JOBS = "Applied_jobs";

    //Experience
    public static final  String KEY_POSITION_TITLE = "Position_title";
    public static final  String KEY_COMPANY_NAME = "Company_name";
    public static final  String KEY_START_DATE = "Start_date";
    public static final  String KEY_END_DATE = "End_date";
    public static final  String KEY_WORK_SUMMARY = "Work_summary";
    public static final  String KEY_PRESENT_WORK = "Present_work";

    //Education
    public static final  String KEY_DEGREE = "Degree";
    public static final  String KEY_MAJOR = "Major";
    public static final  String KEY_UNIVERSITY_NAME = "University";
    public static final  String KEY_EDUCATION_COUNTRY = "Education_country";
    public static final  String KEY_START_YEAR = "Start_year";
    public static final  String KEY_END_YEAR = "End_year";

    //Review
    public static final  String KEY_REVIEW = "Review";
    public static final  String KEY_REVIEW_USERNAME = "Reviewer_username";
    public static final  String KEY_REVIEW_PROFILE_PIC = "Reviewer_profileimage";
    public static final  String KEY_REVIEW_CONTENT= "Content";
    public static final  String KEY_REVIEW_RATING= "Rating";

    //Complaint
    public static final  String KEY_COLLECTION_COMPLAINTS = "Complaints";
    public static final  String KEY_COMPLAINTS_FROM = "Complaint_from";
    public static final  String KEY_COMPLAINTS_ON = "Complaint_on";
    public static final  String KEY_COMPLAINTS_CONTENT = "Complaint_description";

    //Request Account Deletion
    public static final  String KEY_COLLECTION_ACCOUNT_DELETION = "Account_Deletion";
    public static final  String KEY_DELETION_REASON = "Reason_description";
    public static final  String KEY_ACCOUNT_DELETION_FROM = "Request_from";




    public static final  String KEY_PROFILE_IMAGE = "Profile_image";
    public static final  String KEY_IS_SIGNED_IN ="IsSignedIn";
    public static final  String KEY_FCM_TOKEN ="fcmToken";
    public static final  String KEY_PROFILECOMPLETION_VALUE ="Profile_Completion";
    public static final  String KEY_COLLECTION_CHAT ="Messages";
    public static final  String KEY_SENDER_ID ="senderID";
    public static final  String KEY_RECEIVER_ID ="receiverID";
    public static final  String KEY_MESSAGE = "Content";
    public static final  String KEY_IS_SEEN = "IsSeen";
    public static final  String KEY_TIMESTAMP = "timestamp";
    public static final  String KEY_USER = "user";
    public static final  String KEY_COLLECTION_CONVERSATIONS = "Conversations";
    public static final  String KEY_SENDER_NAME = "senderName";
    public static final  String KEY_RECEIVER_NAME = "receiverName";
    public static final  String KEY_SENDER_IMAGE = "senderImage";
    public static final  String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final  String KEY_LAST_MESSAGE = "Last_message";
    public static final  String KEY_LAST_MESSAGE_TYPE = "Lastmessage_type";
    public static final  String KEY_STATUS = "Status";
    public static final  String KEY_VISIBLE_ASFREELANCER = "Visible_as_freelancer";
    public static final  String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final  String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final  String REMOTE_MSG_DATA = "data";
    public static final  String REMOTE_MSG_REGISTRATION_IDS = "registration_ids";
    public static final  String NOTIFICATION_TYPE = "Notification_type";

    public static final  String KEY_SKILLS = "Top_skills";
    public static final  String KEY_LANGUAGES = "languages";
    public static final  String KEY_EXPERIENCES = "Experiences";
    public static final  String KEY_EDUCATION = "Education";

    public static Boolean UPDATE_FLAG =  false;
    public static Boolean PROFILE_FLAG =  false;
    public static Boolean FIXED_PRICE_FLAG =  false;
    public static Boolean MYJOBS_FLAG =  false;
    public static Boolean HIREDJOB_FLAG =  false;
    public static Boolean POSTEDJOB_FLAG =  false;
    public static Boolean FROMPROPOSAL_FLAG =  false;
    public static Boolean FROMEDITPROFILE_FLAG =  false;
    public static Boolean EDITPOST_FLAG = false;
    public static Boolean BACKFROMSKILLS_FLAG = false;
    public static Boolean VISIBLEASFREELANCER_FLAG = false;

    public static boolean User_ComplaintBefore = false;
    public static boolean User_ComplaintBefore1 = false;

    public static int step = 0;
    public static int notificationchat_count = 0;


    //JOB_POSTS
    public static final  String KEY_COLLECTION_JOBS = "Jobs";
    public static final  String KEY_JOB = "Job";
    public static final  String KEY_EMPLOYERID = "EmployerID";
    public static final  String KEY_JOBID = "JobID";
    public static final  String KEY_JOB_TITLE = "Title";
    public static final  String KEY_JOB_DESCRIPTION = "Description";
    public static final  String KEY_JOB_CREATED_DATE = "Created_date";
    public static final  String KEY_JOB_SKILLS_REQUIRED= "Skills_required";
    public static final  String KEY_JOB_PAYMENT = "Budget";
    public static final  String KEY_JOB_PAYMENT_TYPE = "Type";
    public static final  String KEY_JOB_PAYMENT_MINIMUM_BUDGET= "Minimum_budget";
    public static final  String KEY_JOB_PAYMENT_MAXIMUM_BUDGET= "Maximum_budget";
    public static final  String KEY_JOB_PROPOSAL = "Proposal";
    public static final  String KEY_MY_PROPOSALS = "My_Proposals";
    public static final  String KEY_JOB_PROPOSAL_FREELANCER_USERNAME = "Freelancer_username";
    public static final  String KEY_JOB_PROPOSAL_FREELANCER_CONTENT = "Content";
    public static final  String KEY_JOB_PROPOSAL_FREELANCER_BID_AMOUNT = "Bid_amount";
    public static final  String KEY_JOB_AVAILABILITY = "Availability";
    public static final  String KEY_JOB_FREELANCER_HIRED = "Hired_freelancer";
    public static final  String KEY_JOB_UPLOADED_FILES = "Uploaded_files";

    //Updated Job post
    public static final  String KEY_JOB_ID_UPDATE = "JobID_Updated";
    public static final  String KEY_JOB_TITLE_UPDATE = "Title_Updated";
    public static final  String KEY_JOB_DESCRIPTION_UPDATE = "Description_Updated";
    public static final  String KEY_JOB_SKILLS_REQUIRED_UPDATE= "Skills_required_Updated";
    public static final  String KEY_JOB_PAYMENT_UPDATE = "Budget_Updated";
    public static final  String KEY_JOB_PAYMENT_TYPE_UPDATE = "Type_Updated";
    public static final  String KEY_JOB_PAYMENT_MINIMUM_BUDGET_UPDATE= "Minimum_budget_Updated";
    public static final  String KEY_JOB_PAYMENT_MAXIMUM_BUDGET_UPDATE= "Maximum_budget_Updated";
    public static final  String KEY_JOB_UPLOADED_FILES_UPDATE = "Uploaded_files_Updated";


    public static final  String KEY_JOB_ARRAY = "Job_array";

    public static ArrayList<String> JobRequiredSkills_Array = new ArrayList<>();
    public static ArrayList<HashMap> Proposals_Map = new ArrayList<>();
    public static  ArrayList<HashMap> Files_Map = new ArrayList<>();
    public static ArrayList<MediaFile> Mediafiles_Uploaded = new ArrayList<>();
    public static ArrayList<HashMap> Freelancer_Proposal = new ArrayList<>();
    public static ArrayList<HashMap> MyProposals = new ArrayList<>();

    public static ArrayList<String> Skills_Array = new ArrayList<>();
    public static ArrayList<String> LanguageChip_list = new ArrayList<>();
    public static ArrayList<HashMap> Experience_Map = new ArrayList<>();
    public static ArrayList<HashMap> Education_Map = new ArrayList<>();
    public static ArrayList<HashMap> Review_Map = new ArrayList<>();
    public static ArrayList<HashMap> Complaint_Map = new ArrayList<>();
    public static ArrayList<HashMap> MyProposals_Map = new ArrayList<>();

    public static ArrayList<String> ChatNotification_list = new ArrayList<>();

    public static String JOB_INFO = null;
    public static Integer USERNAME_EXISTS = 0;




    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String,String> getremoteMsgHeaders() {
        if(remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAJkDQBlU:APA91bELw-1xqXlKzcfZKBgUY76bjQxzE5DeY98YFpCM8TitemOfxeeMhgCmPU_pzN98cNWttjNj7W95GfA3zbBAopTs8-h48TOB4J5pg4biGM1tWlwUPMZYhcSh3dRbY6HZnfuO-oxI"
            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return  remoteMsgHeaders;
    }
    public static final  String KEY_PREFERENCE_NAME ="WorkyzoAppPreference";

    private static final  String PACKAGE_NAME="com.appstechio.workyzo.utilities";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final int SUCCESS_RESULT = 1;
    public static final int FAILURE_RESULT = 0;






    public static int[] Categoryicons = {R.drawable.ic_computer, R.drawable.ic_writing,R.drawable.ic_graphic_design
            ,R.drawable.ic_keyboard,R.drawable.ic_research,R.drawable.ic_sales,R.drawable.ic_business_and_trade
            ,R.drawable.ic_manufacturing,R.drawable.ic_mobile_computer,R.drawable.ic_translating,R.drawable.ic_services
            ,R.drawable.ic_shipping,R.drawable.ic_signal_tower,R.drawable.ic_classroom,R.drawable.ic_ellipsis};

    public static String[] Categorylabels = {"Websites, IT & Software", "Writing & Content",
            "Design, Media & Architecture", "Data Entry & Admin","Engineering & Science",
            "Sales & Marketing","Business, Accounting, Human Resources & Legal", "Product Sourcing & Manufacturing",
            "Mobile Phones & Computing","Translation & Languages","Local Jobs & Services","Freight, Shipping & Transportation",
            "Telecommunications","Education","Other"};

    public static String[] computerskillslabels = {".NET", ".NET 5.0/6", ".NET Core", ".NET Core Web API", "4D", "A-GPS", "A/B Testing", "A+ Certified IT Technician",
            "A+ Certified Professional", "Ab Initio", "ABAP List Viewer (ALV)", "ABAP Web Dynpro", "Abaqus", "ABIS", "AbleCommerce", "Ableton",
            "Ableton Live", "Ableton Push", "AC3", "ACARS", "Active Directory", "ActiveCampaign", "Ada programming", "ADO.NET", "Adobe Acrobat",
            "Adobe Air", "Adobe Audition", "Adobe Captivate", "Adobe Creative Cloud", "Adobe Dynamic Tag Management", "Adobe Experience Manager",
            "Adobe Freehand", "Adobe Illustrator", "Adobe Muse", "Adobe Pagemaker", "Adobe Premiere Pro", "Adobe XD",
            "Advanced Business Application Programming (ABAP)", "Affectiva", "Agile Development", "Agile Project Management", "Agora", "AI/RPA development",
            "Airtable", "AIX Administration", "AJAX", "AJAX Frameworks", "AJAX Toolkit", "Ajax4JSF", "Akka", "Alacra", "ALBPM", "Alexa Modification",
            "Algorithm Analysis", "Alias", "Alibaba", "Alibre Design", "Alienbrain", "All-Source Analysis", "AlphaCAM", "Alpine JS", "Altera Quartus",
            "Alteryx", "Altium Designer", "Altium NEXUS", "Alvarion", "Amazon App Development", "Amazon FBA", "Amazon Product Launch", "Amazon S3",
            "Amazon Web Services", "Amibroker Formula Language", "AMQP", "Analytics", "Anaplan", "Android App Development", "Android SDK", "Android Studio",
            "Android Wear SDK", "Angular", "Angular Material", "AngularJS", "Ansible", "Ansys", "Apache", "Apache Ant", "Apache Hadoop",
            "Apache Maven", "Apache Solr", "API", "API Development", "API Integration", "App Developer", "App Development", "App Publication",
            "App Reskin", "Appian BPM", "Apple Safari", "Apple UIKit", "Apple Xcode", "Applescript", "Application Packaging",
            "Application Performance Monitoring", "Apttus", "ArangoDB", "Arc", "ArchiCAD", "Architectural Engineering", "ArcMap", "ARCore",
            "Arena Simulation Programming", "Argus Monitoring Software", "Ariba", "ARKit", "Armadillo", "Articulate Storyline", "Artificial Intelligence",
            "AS400 & iSeries", "Asana", "ASM", "ASP", "ASP.NET", "ASP.NET MVC", "Aspen HYSYS", "Assembla", "Assembly", "Asterisk PBX",
            "Atlassian Confluence", "Atlassian Jira", "Atmel", "Augmented Reality", "AutoCAD Advance Steel", "AutoHotkey",
            "Automatic Number Plate Recognition (ANPR)", "Automation", "Aws Lambda", "Azure", "backbone.js", "Backend Development", "Balsamiq",
            "Bash Scripting", "BeautifulSoup", "Big Data Sales", "BigCommerce", "Binary Analysis", "BIRT Development", "Bitcoin", "BitMEX", "Biztalk",
            "Blazor", "Blender", "Blockchain", "Blog Install", "Bluetooth Low Energy (BLE)", "BMC Remedy", "Boonex Dolphin", "Boost", "Bower",
            "BSD", "Bubble Developer", "BuddyPress", "Buildbox", "Business Catalyst", "Business Central", "Business Intelligence", "C Programming",
            "C# Programming", "C++ Programming", "CakePHP", "Call Control XML", "Camtasia", "Carthage", "CasperJS", "Caspio", "Cassandra", "CentOs",
            "Certified Ethical Hacking", "Certified Information Systems Security Professional (CISSP)", "Charts", "Chatbot",
            "Chef Configuration Management", "Chordiant", "Chrome OS", "Cinematography", "CircleCI", "CircuitMaker", "CircuitStudio", "Cisco",
            "Citadela", "CLIPS", "Clojure", "Cloud", "Cloud Computing", "Cloud Data", "Cloud Development", "Cloud Finance", "Cloud Foundry",
            "Cloud Networking", "Cloud Procurement", "Cloud Security", "CMS", "COBOL", "Cocoa", "Cocoa Touch", "CocoaPods", "Cocos2d", "Codeigniter",
            "Coding", "CoffeeScript", "Cognos", "Cold Fusion", "COMPASS", "CompTIA", "Computer Graphics", "Computer Science", "Computer Security",
            "Computer Vision", "Copyright", "Corel Draw", "CRE Loaded", "Creo", "Crestron", "CS-Cart", "CSS3", "CubeCart", "CUDA", "cURL", "CV Library",
            "cxf", "D3.js", "Dart", "Data Governance", "Data Integration", "Data Modernization", "Data Visualization", "Data Warehousing",
            "Database Administration", "Database Development", "Database Programming", "DataLife Engine", "Datatables", "DDS", "Debian", "Debugging",
            "Delphi", "Development", "Development Operations", "Digital Marketing", "Digital Signal Processing", "Django", "DNS", "Docker",
            "Documentation", "Dojo", "DOM", "DOS", "DotNetNuke", "Dovecot", "Drawing", "Dropbox API", "Drupal", "Dthreejs", "Dynamic 365", "Dynamics",
            "Dynatrace Software Monitoring", "EC Pay Workday", "Eclipse", "ECMAScript", "eCommerce", "edX", "Elasticsearch", "eLearning", "Electron JS",
            "Electronic Data Interchange (EDI)", "Electronic Forms", "Elementor", "Elixir", "Elm", "Email Developer", "Embedded Software", "Ember.js",
            "Enterprise Architecture", "Erlang", "ERP Software", "ES8 Javascript", "Espruino", "Ethereum", "ETL", "Express JS", "Expression Engine",
            "Ext JS", "F#", "Face Recognition", "Facebook API", "Facebook Development", "Facebook Pixel", "Facebook Product Catalog", "Facebook SDK",
            "Fastlane", "FaunaDB", "Figma", "FileMaker", "Financial Software Development", "Firefox", "Firmware", "FLANN", "Flask", "Flutter", "Forth",
            "Fortran", "Forum Software", "Freelancer API", "FreeSwitch", "Frontend Development", "Full Stack Development", "Funnel", "Fusion 360",
            "Game Consoles", "Game Design", "Game Development", "GameSalad", "Gamification", "GatsbyJS", "Genetic Algebra Modelling System", "Geofencing",
            "Geographical Information System (GIS)", "GIMP", "Git", "GitHub", "GitLab", "Golang", "Google Analytics", "Google APIs", "Google App Engine",
            "Google Buzz", "Google Canvas", "Google Cardboard", "Google Checkout", "Google Chrome", "Google Cloud Platform", "Google Cloud Storage",
            "Google Docs", "Google Earth", "Google Firebase", "Google Maps API", "Google PageSpeed Insights", "Google Plus", "Google Sheets",
            "Google Tag Management", "Google Wave", "Google Web Toolkit", "Google Webmaster Tools", "GoPro", "GPGPU", "Grails", "Graphics Programming",
            "GraphQL", "Graylog", "Grease Monkey", "Growth Hacking", "Grunt", "GTK+", "Guidewire", "Hadoop", "Handlebars.js", "Hardware Security Module",
            "Haskell", "HBase", "Heroku", "Heron", "Hewlett Packard", "Highcharts", "Hive", "HomeKit", "Houdini", "HP Openview", "HP-UX", "HTC Vive",
            "HTML", "HTML5", "HTTP", "Hubspot", "HyperMesh", "iBeacon", "IBM Bluemix", "IBM BPM", "IBM Cloud", "IBM Datapower", "IBM Integration bus",
            "IBM MQ", "IBM Tivoli", "IBM Tririga", "IBM Websphere Transformation Tool", "IIS", "iMacros", "IMAP", "Infor", "Informatica MDM",
            "Informatica Powercenter ETL", "Instagram", "Instagram API", "Internet Security", "Interspire", "Ionic Framework", "iOS Development",
            "IT Operating Model", "IT Project Management", "IT strategy", "IT Transformation", "ITIL", "J2EE", "Jabber", "Jade Development", "Jamstack",
            "Jasmine Javascript", "Java", "Java ME", "Java Spring", "Java Technical Architecture", "JavaFX", "JavaScript", "Javascript ES6",
            "JD Edwards CNC", "Jenkins", "Jinja2", "Joomla", "jqGrid", "jQuery", "jQuery / Prototype", "JSON", "JSP", "JUCE", "Julia Development",
            "Julia Language", "JUnit", "K2", "Karma Javascript", "Kendo UI", "Keras", "Keycloak", "Kinect", "KNIME", "Knockout.js", "Kubernetes",
            "LabVIEW", "Laravel", "Leap Motion SDK", "LearnDash", "Learning Management Solution (LMS) Consulting", "Learning Management Systems (LMS)",
            "LESS/Sass/SCSS", "LIBSVM", "Link Building", "Linkedin", "LINQ", "Linux", "Lisp", "LiveCode", "Local Area Networking", "Lotus Notes",
            "Low Code", "Lua", "Lucene", "Mac OS", "Magento", "Magento 2", "Magic Leap", "MailerLite", "Managed Analytics", "Map Reduce", "MapKit",
            "MariaDB", "MEAN Stack", "Messenger Marketing", "Metatrader", "MeteorJS", "Micros RES", "Microsoft", "Microsoft Access", "Microsoft Azure",
            "Microsoft Exchange", "Microsoft Expression", "Microsoft Hololens", "Microsoft Project", "Microsoft SQL Server", "Microsoft Visio",
            "MicroStrategy", "Minecraft", "Mininet", "Minitab", "MMORPG", "Mobile App Testing", "Mobile Development", "MODx", "MonetDB", "MongoDB",
            "Moodle", "MOVEit", "Moz", "MQL4", "MQTT", "MuleSoft", "MVC", "MySpace", "MySQL", "National Building Specification", "NAV", "Netbeans",
            "NetSuite", "Network Administration", "Network Engineering", "Network Security", "Nginx", "NgRx", "Ning", "Node.js",
            "Non-fungible Tokens (NFT)", "NoSQL", "NoSQL Couch & Mongo", "NumPy", "OAuth", "Object Oriented Programming (OOP)",
            "Objective C", "OCR", "OctoberCMS", "Oculus Mobile SDK", "Oculus Rift", "Odoo", "Office 365", "Office Add-ins",
            "Offline Conversion Facebook API Integration", "Open Cart", "Open Journal Systems", "Open Source", "OpenBravo",
            "OpenBSD", "OpenCL", "OpenCV", "OpenGL", "OpenSceneGraph", "OpenSSL", "OpenStack", "OpenVMS", "OpenVPN", "OpenVZ", "Oracle", "Oracle APEX",
            "Oracle Database", "Oracle EBS Tech Integration", "Oracle Hyperion", "Oracle OBIA", "Oracle OBIEE", "Oracle Primavera", "Oracle Retail",
            "OSCommerce", "P2P Network", "Packaging Technology", "Papiamento", "Parallax Scrolling", "Parallel Processing", "Parallels Automation",
            "Parallels Desktop", "Pardot Development", "Pascal", "Pattern Matching", "Payment Gateway Integration", "PayPal API", "Paytrace",
            "PC Programming", "PEGA PRPC", "PencilBlue CMS", "Penetration Testing", "Pentaho", "Perl", "PhoneGap", "Photoshop Coding", "PHP", "PHP Slim",
            "phpFox", "phpMyAdmin", "PhpNuke", "PHPrunner", "PICK Multivalue DB", "Pine Script", "Pinterest", "Plesk", "Plugin", "Polarion", "POP / POP3",
            "Postfix", "PostgreSQL", "PostgreSQL Programming", "Power BI", "PowerApps", "Powershell", "Prestashop", "Programming", "Prolog",
            "Prometheus Monitoring", "Protoshare", "Prototyping", "Protractor Javascript", "Puck.js", "Puppet", "PureScript", "Push Notification",
            "PySpark", "Python", "Pytorch", "QlikView", "Qt", "Qualtrics Survey Platform", "QuickBase", "R Programming Language", "Racket", "RapidWeaver",
            "Raspberry Pi", "Ray-tracing", "React Native", "React.js", "React.js Framework", "REALbasic", "Reason", "Red Hat", "Redis", "Redmine",
            "Redshift", "Redux.js", "Regression Testing", "Regular Expressions", "RESTful", "RESTful API", "Reverse Engineering", "Revit",
            "Revit Architecture", "RichFaces", "Roadnet", "Roblox", "Robot Operating System (ROS)", "Rocket Engine", "RPG Development", "RSS", "Ruby",
            "Ruby on Rails", "Rust", "RxJS", "Ryu Controller", "Sails.js", "Salesforce App Development", "Salesforce Commerce Cloud",
            "Salesforce Marketing Cloud", "Samsung Accessory SDK", "SAP", "SAP 4 Hana", "SAP BODS", "SAP Business Planning and Consolidation", "SAP CPI",
            "SAP HANA", "SAP Hybris", "SAP Pay", "SAP PI", "SAP Screen Personas", "SAP Transformation", "Sass", "Scala", "Scheme", "Scikit Learn", "SciPy",
            "SCORM", "Scrapy", "Script Install", "Scripting", "Scrum", "Scrum Development", "SD-WAN", "SDW N17 Service Qualification", "Segment",
            "Selenium", "Selenium Webdriver", "Sencha / YahooUI", "SEO", "SEO Auditing", "Server", "Server to Server Facebook API Integration",
            "ServiceNow", "SFDC", "Sharepoint", "Shell Script", "Shopify", "Shopify Development", "Shopping Carts", "Siebel", "Silverlight", "Sketching",
            "Slack", "Smart Contracts", "Smarty PHP", "SMTP", "Snapchat", "Social Engine", "Social Media Management", "Social Networking", "Socket IO",
            "Software Architecture", "Software Development", "Software Testing", "Solaris", "Soldering", "Solidity", "Solutions Architecture", "Spark",
            "Sphinx", "Splunk", "SPSS Statistics", "SQL", "SQLite", "Squarespace", "Squid Cache", "SSIS (SQL Server Integration Services)", "Steam API",
            "Storage Area Networks", "Storm", "Stripe", "Subversion", "SugarCRM", "Svelte", "SVG", "Swift", "Swift Package Manager", "Swing (Java)",
            "Symfony PHP", "System Admin", "System Analysis", "T-SQL (Transact Structures Query Language)", "Tableau", "Tally Definition Language",
            "TaoBao API", "Tealium", "TeamCity", "Technology Consulting", "Tensorflow", "Test", "Test Automation", "Testing / QA", "TestStand", "Three.js",
            "Tibco Spotfire", "Time & Labor SAP", "Titanium", "Tizen SDK for Wearables", "Travis CI", "Troubleshooting", "Tumblr", "TvOS", "Twilio",
            "Twitch", "Twitter", "Twitter API", "Typescript", "Typing", "TYPO3", "Ubuntu", "Umbraco", "UML Design", "Underscore.js", "Unity 3D",
            "UNIX", "Unreal Engine", "Usability Testing", "User Interface / IA", "User Story Writing", "V-Play", "Vapor", "Varnish Cache", "VB.NET",
            "VBScript", "vBulletin", "Veeam", "Version Control Git", "VertexFX", "Vim", "Virtual Machines", "Virtual Reality", "Virtual Worlds",
            "Virtuemart", "Virtuozzo", "Visual Basic", "Visual Basic for Apps", "Visual Foxpro", "Visual Studio", "Visualization", "VMware", "VoiceXML",
            "VoIP", "Volusion", "Vowpal Wabbit", "VPS", "vTiger", "VtrunkD", "Vue.js", "Vue.js Framework", "Vuforia", "WatchKit", "Web API",
            "Web Crawling", "Web Development", "Web Hosting", "Web Scraping", "Web Security", "Web Services", "Webflow", "webMethods",
            "Website Analytics", "Website Build", "Website Management", "Website Optimization", "Website Testing", "Weebly", "WHMCS", "Windows 8",
            "Windows API", "Windows Desktop", "Windows Server", "Windows Service", "WinJS", "Wix", "WordPress", "WPF", "Wufoo", "x86/x64 Assembler",
            "Xamarin", "XAML", "Xara", "Xcodebuild", "XHTML", "XML", "XMPP", "Xojo", "Xoops", "XPages", "xpath", "XQuery", "XSLT",
            "XSS (Cross-site scripting)", "Yarn", "Yii", "Yii2", "YouTube", "Zapier", "Zen Cart", "Zend", "Zendesk", "Znode", "Zoho", "Zoho Creato"};

    public static String[] Writingskillslabels= {"Abnormal Psychology", "Abstract", "Academic Medicine", "Academic Publishing", "Academic Research", "Academic Writing",
            "Annuals", "Apple iBooks Author", "Article Rewriting", "Article Writing", "Biography Writing", "Blog", "Blog Writing", "Book Review",
            "Book Writing", "Business Plan Writing", "Business Writing", "Cartography & Maps", "Catch Phrases", "Comedy Writing", "Communications",
            "Compliance and Safety Procedures Writer", "Content Creation", "Content Strategy", "Content Writing", "Copy Editing", "Copy Typing",
            "Copywriting", "Creative Writing", "eBooks", "Editing", "Editorial Writing", "Educational Research", "Essay Writing", "Fact Checking",
            "Fashion Writing", "Fiction", "Financial Research", "Forum Posting", "Ghostwriting", "Grant Writing", "Headlines",
            "Investigative Journalism", "Journalism", "LaTeX", "Legal Writing", "Medical Research", "Medical Writing", "Newsletters", "Online Writing",
            "PDF", "Pitch Deck Writing", "Poetry", "Powerpoint", "Press Releases", "Product Descriptions", "Proofreading", "Proposal Writing",
            "Publishing", "Report Writing", "Research", "Research Writing", "Resumes", "Reviews", "RFP Writing", "Romance Writing", "Scientific Writing",
            "Screenwriting", "SEO Writing", "Short Stories", "Slogans", "Social Media Copy", "Speech Writing", "Survey Research", "Taglines",
            "Technical Documentation", "Technical Writing", "Test Plan Writing", "Test Strategy Writing", "Translation", "Travel Writing",
            "Web Page Writer", "WIKI", "Wikipedia", "Word Processing", "Writing"};

    public static  String[] DesignMediaskillslabels = {"2D Animation", "2D Animation Explainer Video", "2D Drafting", "2D Drawing", "2D Game Art", "2D Layout",
            "360-degree video", "3D Animation", "3D Architecture", "3D CAD", "3D Design", "3D Drafting", "3D Layout", "3D Logo", "3D Model Maker",
            "3D Modelling", "3D Rendering", "3D Rigging", "3D Scanning", "3D Visualization", "3ds Max", "A/V design", "A/V editing", "A/V Engineering",
            "A/V Systems", "A&R", "ActionScript", "Adobe Dreamweaver", "Adobe Fireworks", "Adobe Flash", "Adobe FrameMaker", "Adobe InDesign",
            "Adobe Lightroom", "Adobe LiveCycle Designer", "Adobe Robohelp", "Advertisement Design", "After Effects", "Album Design", "Album Production",
            "Alternative Rock", "Alto Flute", "Animated Video Development", "Animation", "App Designer", "Apple Compressor", "Apple Logic Pro",
            "Apple Motion", "Architectural Rendering", "Architectural Visualization", "Art Consulting", "Artist & Repertoire Administration",
            "Arts & Crafts", "Audio Editing", "Audio Mastering", "Audio Production", "Audio Services", "Audiobook Narration", "AutoCAD Architecture",
            "Autodesk Inventor", "Autodesk Revit", "Autodesk Sketchbook Pro", "Axure", "Banner Design", "Blog Design", "Book Artist", "Book Cover Design",
            "Bootstrap", "Brochure Design", "Building Architecture", "Building Information Modeling", "Building Regulations", "Business Cards",
            "Calligraphy", "Canva", "Capture NX2", "Caricature & Cartoons", "CGI", "Children Book Illustration", "Cinema 4D", "Commercials",
            "Concept Art", "Concept Design", "Corel Painter", "Corporate Identity", "Costume Design", "Covers & Packaging", "Creative Design", "CSS",
            "DaVinci Resolve", "Design", "Digital Art", "Digital Cinema Packages", "eBook Design", "eLearning Designer", "Explainer Videos",
            "Fashion Consulting", "Fashion Design", "Fashion Modeling", "Filmmaking", "Final Cut Pro", "Finale / Sibelius", "FL Studio", "Flash 3D",
            "Flash Animation", "Flex", "Flow Charts", "Flyer Design", "Format & Layout", "Front-end Design", "Furniture Design", "GarageBand",
            "GIF Animation", "Graphic Art", "Graphic Design", "Icon Design", "Illustration", "Illustrator", "Image Consultation", "Image Processing",
            "iMovie", "Industrial Design", "Infographics", "Infrastructure Architecture", "Instructional Design", "Interior Design", "Invision",
            "Invitation Design", "JDF", "Kinetic Typography", "Label Design", "Landing Pages", "Logo Design", "Makerbot", "Maya", "Motion Graphics",
            "Music", "Music Management", "Neo4j", "Package Design", "Packaging Design", "Pattern Making", "Photo Anywhere", "Photo Editing",
            "Photo Restoration", "Photo Retouching", "Photography", "Photoshop", "Photoshop Design", "Post-Production", "Poster Design", "Pre-production",
            "Presentations", "Prezi", "Print", "Product Photography", "Prototype Design", "PSD to HTML", "PSD2CMS", "QuarkXPress", "RWD",
            "Shopify Templates", "Sign Design", "Sketch", "SketchUp", "Sound Design", "Sound Engineering", "Stationery Design", "Sticker Design",
            "Storyboard", "T-Shirts", "Tattoo Design", "Tekla Structures", "Templates", "Textile Design", "Typography", "Urban Design",
            "User Experience Design", "User Interface Design", "Vectorization", "Vectorworks", "Vehicle Signage", "Video Broadcasting", "Video Editing",
            "Video Post-editing", "Video Production", "Video Services", "Video Tours", "Videography", "VideoScribe", "Visual Arts", "Voice Talent",
            "Website Design", "Wireframes", "Word", "Yahoo! Store Design", "Zbrush"};

    public static String[] DataEntryskillslabels = {"ABBYY FineReader", "Academic Administration", "ANOVA", "Article Submission", "Bookkeeping", "BPO", "Call Center",
            "Customer Service", "Customer Support", "Data Analytics", "Data Architecture", "Data Cleansing", "Data Delivery", "Data Entry",
            "Data Extraction", "Data Processing", "Data Scraping", "Database Design", "Desktop Support", "Email Handling", "ePub", "Excel",
            "Excel Macros", "Excel VB Capabilities", "Excel VBA", "General Office", "Helpdesk", "Infographic and Powerpoint Slide Designing",
            "Investment Research", "LibreOffice", "Microsoft Office", "Microsoft Outlook", "Order Processing", "Phone Support",
            "PostgreSQL Administration", "Procurement", "Qlik", "Qualitative Research", "qwerty", "Relational Databases", "SAP Master Data Governance",
            "Software Documentation", "Technical Support", "Telephone Handling", "Time Management", "Transcription", "Video Upload", "Virtual Assistant",
            "Web Search"};

    public static String[] Engineeringskillslabels = {"802.11", "A&P", "A1 Assessor", "AAUS Scientific Diver", "ABC Analysis", "AC Drives", "Accelerator Physics",
            "Acoustical Engineering", "Aeronautical Engineering", "Aerospace Engineering", "Agronomy", "AI (Artificial Intelligence) HW/SW",
            "Aircraft Performance", "Aircraft Propulsion", "Aircraft Structures", "Aircraft Systems", "Airfield Lighting", "Airframe",
            "Airspace Management", "Alarm Management", "Alarm Systems", "Alchemist", "Algorithm", "Analog", "Analog / Mixed Signal / Digital",
            "Anodizing", "Anomaly Detection", "Anritsu Certified", "Antenna Design", "Anthropology", "Apple Homekit", "Apple MFI", "Arduino", "ARM",
            "ASIC", "Astrophysics", "Audio Processing", "AutoCAD", "Automotive", "Bare Metal", "Battery Charging and Batteries", "BeagleBone Black",
            "Bill of Materials (BOM) Analysis", "Bill of Materials (BOM) Evaluation", "Bill of Materials (BOM) Optimization", "Biology",
            "Biomedical Engineering", "Biotechnology", "Bluetooth", "Bluetooth Module", "Board Support Package (BSP)", "Broadcast Engineering",
            "Building Engineering", "CAD/CAM", "Calculus", "CATIA", "Cellular Design", "Cellular Modules", "Cellular Service", "Chemical Engineering",
            "Circuit Board Layout", "Circuit Design", "Civil Engineering", "Clean Technology", "Climate Sciences", "Cloud Service",
            "Combinatorial Optimization", "Combinatorial Problem Solving", "Compliance Engineering", "Component Engineering", "Computational Analysis",
            "Computational Linguistics", "Construction Engineering", "Construction Monitoring", "Consumer Products", "Continuous Integration",
            "Control Engineering", "Control System Design", "Controller Area Network (CAN)", "Cryptography", "D0-178 Certification",
            "D0-254 Certification", "Data Mining", "Data Science", "DDR3 (PCIe, board design/fpga)", "Deep Learning", "DesignBuilder", "DevOps",
            "DFM (Design for Manufacturing)", "Digital ASIC Coding", "Digital Design", "Digital Electronics", "Digital Forensics", "Digital Networking",
            "Drilling Engineering", "Drones", "DSL/MODEMs", "Edge Computing", "Electrical Engineering", "Electronic Design", "Electronics",
            "Embedded Systems", "Encryption", "Energy", "Energy Modelling", "Engineering", "Engineering Drawing", "Engineering Mathematics",
            "Environmental Engineering", "Estimation", "Finite Element Analysis", "Flex Circuit Design", "FPGA", "FPGA Coding", "Genealogy",
            "Genetic Algorithms", "Genetic Engineering", "Geology", "Geospatial", "Geotechnical Engineering", "GPS", "Graphical User Interface (GUI)",
            "HALT/HASS Testing", "Health", "Heat Load Calculation", "Home Design", "Human Sciences", "HVAC", "HyperLynx", "I2C", "Imaging", "IMX6",
            "Industrial Engineering", "Instrumentation", "Intercom", "Internet of Things (IoT)", "Intrinsic Safety Applications", "IP Cores",
            "ISM Radio Module", "Linear Programming", "Local Interconnect Network (LIN)", "LoRa", "Machine Learning (ML)",
            "Machine Vision / Video Analytics", "Manufacturing Design", "Manufacturing Engineering", "Marine Engineering", "Materials Engineering",
            "Mathematics", "MATLAB", "Matlab and Mathematica", "Mechanical Design", "Mechanical Engineering", "Mechatronics", "Medical",
            "Medical Engineering", "Medical Products", "MEMs", "Microbiology", "Microcontroller", "Microstation", "Mining Engineering",
            "Mixing Engineering", "Motor Control", "MPSoC Design", "Nanotechnology", "Natural Language", "Near Field Communication (NFC)",
            "Neural Networks", "Optical Engineering", "PCB Design and Layout", "PCB Layout", "PCI Express", "Petroleum Engineering", "Physics",
            "PLC & SCADA", "Power Amplifier RF", "Power Converters", "Power Generation", "Power Redesign", "Power Supply", "Product End of Life (EOL)",
            "Product Management", "Project Scheduling", "Psychology", "Qi", "Quality and Reliability Testing", "Quantum", "RADAR/LIDAR",
            "Radio Frequency", "Radio Frequency Engineering", "Rapid Prototyping"};

    public static String[] Salesskillslabels = {"ABR Accredited Buyer Representative", "ABR Designation", "Ad Planning & Buying", "Advertising", "Affiliate Marketing",
            "Agency Relationship Management", "Airbnb", "Aircraft Sales", "Analytics Sales", "ATS Sales", "B2B Marketing", "Book Marketing",
            "Brand Management", "Brand Marketing", "Branding", "Bulk Marketing", "Channel Account Management", "Channel Sales", "Classifieds Posting",
            "ClickFunnels", "Cloud Sales", "Competitor Analysis", "Content Marketing", "Conversion Rate Optimization", "CRM", "Crowdfunding",
            "Customer Retention Marketing", "Datacenter Sales", "Digital Agency Sales", "eBay", "Email Marketing", "Emerging Accounts", "Enterprise Sales",
            "Enterprise Sales Management", "Etsy", "Facebook Marketing", "Facebook Shops", "Field Sales", "Field Sales Management", "Financial Sales",
            "Google Adsense", "Google Adwords", "Google Shopping", "Healthcare Sales", "HR Sales", "Hubspot Marketing", "IDM Sales", "Indiegogo",
            "Inside Sales", "Instagram Marketing", "Interactive Advertising", "Internet Marketing", "Internet Research", "ISV Sales", "Kartra",
            "Keyword Research", "Kickstarter", "Lead Generation", "Leads", "Life Science Sales", "Mailchimp", "Mailwizz", "Market Research", "Marketing",
            "Marketing Strategy", "Media Relations", "Media Sales", "Medical Devices Sales", "MLM", "Mobile Sales", "Network Sales", "OEM Account Management",
            "OEM Sales", "Pardot Marketing", "Payroll Sales", "Periscope", "PPC Marketing", "Product Marketing", "Recruiting Sales", "Resellers",
            "Retail Sales", "SaaS Sales", "Sales", "Sales Account Management", "Sales Management", "Sales Promotion", "Search Engine Marketing",
            "Security Sales", "SEOMoz", "Social Media Marketing", "Social Sales", "Social Video Marketing", "Software Sales", "Technology Sales",
            "Telecom Sales", "Telemarketing", "Twitter Marketing", "Unboxing Videos", "Viral Marketing", "Visual Merchandising", "WooCommerce"};

    public static String[] Businessskillslabels = {"A/R analysis", "A/R Collections", "A/R Management", "Account Management", "Account Payables Management",
            "Account Receivables Management", "Accounting", "Actimize", "Administrative Support", "Airline", "Alternative Investments",
            "Annual Report Design", "Anti Money Laundering", "Antitrust Economics", "Asset Management", "Attorney", "Audit", "Autotask",
            "Bank Reconciliation", "Billing", "Brain Storming", "Budgeting and Forecasting", "Business Analysis", "Business Analytics", "Business Coaching",
            "Business Consulting", "Business Plans", "Business Requirement Documentation", "Business Strategy", "Care Management", "Career Consulting",
            "Certified Public Accountant", "Christmas", "Closer", "Compensation and Benefits", "Compensation Consulting", "Compliance",
            "Compliance and Safety Training", "Contracts", "Core Consulting Skills", "Core Systems Transformation", "Corporate Income Tax",
            "Corporate Transactions", "Credit Repair", "Crystal Reports", "CTO", "Custom Duties Tax", "Customer Experience", "Customer Retention",
            "Customer Strategy", "Customs and Global Trade Services", "Data Analysis", "Database Management", "EBS Finance", "EBS Procurement",
            "Ecological Consulting", "Econometrics", "Economics", "ECPay", "Education & Tutoring", "Employee Experience", "Employee Training",
            "Employment Law", "Employment Tax", "Energy and Resource Tax", "Entrepreneurship", "Environmental Consulting", "Equity Transaction Advice",
            "ERP", "Event Planning", "Executive Compensation", "Executive Reward", "Expatriate Tax", "External Auditing", "Finance", "Finance Transformation",
            "Financial Accounting", "Financial Analysis", "Financial Consulting", "Financial Crime", "Financial Forecasting", "Financial Markets",
            "Financial Modeling", "Financial Services Tax", "Fircosoft", "FIX API", "Forensic Consulting", "Fraud Detection", "Fundraising", "Game Testing",
            "General Tax Advisory", "Global Mobility", "Global Tax Compliance", "Health Care Management", "Health Planning", "Health Plans Digitization",
            "Hedge Fund Management", "History", "Human Resources", "IBM Db2", "Immigration", "Immigration Law", "Indirect Tax", "Insurance", "Interviewing",
            "Intuit QuickBooks", "Inventory Management", "Investment Management", "ISO9001", "Jewellery", "Leadership Development", "Legal", "Legal Research",
            "Legal Translation", "Life Coaching", "Life Science Tax Services", "LinkedIn Recruiting", "Linnworks Order Management", "Logistics Company",
            "M&A Tax", "Management", "Management Consulting", "Manufacturing Strategy", "Market Sizing", "Marketplace Service", "Media and Entertainment Tax",
            "Medical Translation", "Mergers and Acquisitions", "MYOB", "nCino", "Nintex Forms", "Nintex Workflow", "Nutrition", "Operations Research",
            "Organization Design", "Organizational Change Management", "OTRS", "Paralegal Services", "Patents", "PAYE Tax", "Payroll", "Payroll HR S&E",
            "PeopleSoft", "Personal Development", "Personal Income Tax", "Personal Tax", "Planning Consulting", "Portfolio Management", "Private Client",
            "Programmatic Advertising", "Project Management", "Project Management Office", "Property Development", "Property Law", "Property Management",
            "Public Relations", "Public Sector and Taxation", "Real Estate", "Real Estate Tax", "Recruitment", "Report Development",
            "Research and Development", "Reward", "Risk Management", "Safety Consulting", "Salesforce CPQ", "Salesforce.com", "SAS", "Share Schemes",
            "Shared Services", "Social Security Tax", "Sourcing", "Sports", "Startup Consulting", "Startups", "Talent Acquisition", "Tax", "Tax Accounting",
            "Tax Centre of Excellence", "Tax Compliance", "Tax Compliance and Outsourcing", "Tax Law", "Tax Management Consulting", "Tax Preparation",
            "Tax Reporting", "Tax Risk Management", "Tax Technology", "Technical Recruiter", "Total Reward", "Trademark", "Trademark Registration",
            "Trading", "Training", "Training Development", "Transaction Tax", "Transfer Pricing", "Unit4 Business World", "Valuation & Appraisal",
            "Value Added Tax", "Value Based Healthcare", "Visa / Immigration", "Wealth Management", "Weddings", "Workday Compensation", "Workday Core HR",
            "Workday Financials", "Workday Payroll", "Workday Security", "Workday Talent & Recruiting", "Workday Time & Absence", "Workflow Consulting",
            "Xero"};

    public static String[] ProductSourcingskillslabels = {"3D Printing", "Alerting", "Amazon", "Analog Electronics", "Andon", "Buyer Sourcing", "CNC Programming",
            "Computer Aided Manufacturing", "Computerized Embroidery", "Freedom to Operate Search", "Manufacturing", "Patent Design Search",
            "Patent Infringement Research", "Patent Invalidity Search", "Patent Landscape", "Patent Validity Search", "Process Automation",
            "Process Validation", "Product Design", "Product Sourcing", "Supplier Sourcing", "Supply Chain"};

    public static String[] Mobileskillslabels = {"Amazon Fire", "Amazon Kindle", "Android", "App Store Optimization", "App Usability Analysis", "Appcelerator Titanium",
            "Apple Watch", "Blackberry", "Geolocation", "iPad", "iPhone", "J2ME", "Kotlin", "Metro", "Mobile App Development", "Nokia", "Palm",
            "Salesforce Lightning", "Samsung", "Symbian", "Virtualization", "WebOS", "Windows CE", "Windows Mobile", "Windows Phone"};

    public static String[] Translationskillslabels = {"Afrikaans Translator", "Albanian Translator", "American Sign Language Translator", "Arabic Translator", "Basque",
            "Bengali", "Bosnian", "Bulgarian", "Catalan", "Croatian", "Czech", "Danish", "Dari", "Dinka", "Dutch", "English (UK)", "English (US)",
            "English Grammar", "English Spelling", "Estonian", "Filipino", "Finnish", "French", "French (Canadian)", "Georgian", "German", "Greek",
            "Hebrew", "Hindi", "Hungarian", "Indonesian", "Interpreter", "Italian", "Japanese", "Kannada", "Korean", "Latvian", "Linguistics",
            "Lithuanian", "Macedonian", "Malay", "Malayalam", "Maltese", "Norwegian", "Pashto", "Poet", "Polish", "Portuguese", "Portuguese (Brazil)",
            "Punjabi", "Romanian", "Russian", "Serbian", "Simplified Chinese (China)", "Slovakian", "Slovenian", "Spanish", "Spanish (Spain)",
            "Swahili", "Swedish", "Tamil", "Telugu", "Thai", "Traditional Chinese (Hong Kong)", "Traditional Chinese (Taiwan)", "Turkish", "Ukrainian",
            "Urdu", "Vietnamese", "Voice Artist", "Welsh", "Yiddish Translator"};

    public static String[] Servicesskillslabels = {"A&E", "Abatement", "ABO Certified", "Air Conditioning", "Alzheimers Care", "Antenna Measurements",
            "Antenna Services", "Antique Restoration", "Appliance Installation", "Appliance Repair", "Asbestos Removal", "Asphalt Contractor",
            "Attic Access Ladders Making", "Awnings", "Balustrading", "Bamboo Flooring", "Bathroom", "Biometrics", "Bracket Installation", "Bricklaying",
            "Building", "Building Certification", "Building Consulting", "Building Design", "Building Surveying", "Car Washing", "Carpentry",
            "Carpet Cleaning", "Carpet Repair & Laying", "Carports", "Carwashing", "Casting", "CCTV", "CCTV Repair", "Ceiling Installation",
            "Cement Bonding Agents", "Clothesline Installation", "Column Installation", "Commercial Cleaning", "Computer Repair", "Computer Support",
            "Concreting", "Contact Center Services", "Cooking / Baking", "Cooking & Recipes", "Courses", "Damp Proofing", "Decking", "Decoration",
            "Demolition", "Disposals", "Domestic Cleaning", "Drafting", "Drone Photography", "Electric Repair", "Embroidery", "Epic Systems",
            "Equipment Rental", "Event Staffing", "Excavation", "Extensions & Additions", "Fencing", "Feng Shui", "Field Technical Support",
            "Financial Planning", "Fire Fighting", "Flashmob", "Floor Coatings", "Flooring", "Flyscreen Installation", "Frames & Trusses",
            "Furniture Assembly", "Gardening", "Gas Fitting", "General Labor", "Glass / Mirror & Glazing", "Gutter Installation", "Hair Styles",
            "Handyman", "Heating Systems", "Home Automation", "Home Organization", "Horticulture", "Hot Water Installation", "House Cleaning",
            "Housework", "IKEA Installation", "Inspections", "Installation", "Interiors", "Kitchen", "Landscape Design", "Landscaping",
            "Landscaping & Gardening", "Laundry and Ironing", "Lawn Mowing", "Lifestyle Coach", "Lighting", "Local Job", "Locksmith", "Lost-wax Casting",
            "Machinery Equipment Hire", "Make Up", "Masonry", "Material Coating", "Millwork", "Mobile Repair", "Mobile Welding", "Mortgage Brokering",
            "Mural Painting", "Painting", "Pavement", "Pest Control", "Pet Sitting", "Piping", "Printer Repair", "Roofing", "Sculpturing",
            "Security Camera", "Security Systems", "Sewing", "Shopping", "Teaching/Lecturing", "Tiling", "Travel Ready", "Upholstery Cleaning",
            "Visa Ready Resources", "Water Treatment", "Workshops", "Yard Work & Removal"};

    public static String[] shipping_transportationskillslabels = {"Bicycle Courier", "Car Courier", "Car Driving", "Cargo Freight", "Coffee Delivery",
            "Container Transport", "Container Truck", "Courier", "Delivery", "DOP Management", "Dropshipping", "Dry Van Trucking", "Flatbed Trucking",
            "Flower Delivery", "Food Takeaway", "Freight", "Frozen Trucking", "Furniture Removalist", "Haulier", "Heavy Haulage",
            "Heavy Haulage Trucking", "Hiab Crane Trucking", "Import/Export", "Last Mile Optimization", "Line Haulage", "Logistics",
            "Motorcycle Courier", "Moving", "Packing & Shipping", "Parcel Delivery", "Pickup", "Reefer Trucking", "Removal Services", "Shipping",
            "Truck Courier", "Trucking", "Van Courier"};

    public static String[] Telecommunicationsskillslabels = {"Access Point Identification", "Active Site Survey", "Aerial Technical Site Survey",
            "Base Station Equipment Management", "Blueprint Calibration", "Floorplan Blueprinting", "HetNet Access Point Installation",
            "Hidden Wireless Network Detection", "Live Survey", "Passive Site Survey", "Physical Site Survey", "PnP BTS configuration",
            "Pre-inspection visits", "Predictive Site Survey", "Radio Access Network Commissioning", "RAN Call Testing", "RAN NMS Integration",
            "Remote Quality Audit", "RF Manual Site Survey", "RF Site Survey", "Rogue Access Point Detection", "Signal Propagation Assessment",
            "Small Cell Deployment", "Technical Site Audit", "Technical Site Survey", "Walking Path Analysis", "Wireless Access Point Installation",
            "Wireless Capacity Analysis", "Wireless Coverage Assessment", "Wireless Network Risk Analysis & Reduction",
            "Wireless Network Security Analysis", "Wireless Security Audit", "Wireless Site Survey"};

    public static String[] Educationskillslabels = {"Accounting Tutoring", "Algebra Tutoring", "Calculus Tutoring", "Chemistry Tutoring", "Chinese Tutoring",
            "Coding Lesson", "College Tutoring", "Computer Science Tutoring", "Education Consulting", "English Teaching", "English Tutoring",
            "French Tutoring", "German Tutoring", "GMAT Tutoring", "GRE Tutoring", "Japanese Teaching", "Japanese Tutoring", "Java Tutoring",
            "Korean Tutoring", "Language Tutoring", "Latin Tutoring", "LSAT Tutoring", "Math Tutoring", "MCAT Tutoring", "Physics Tutoring",
            "Programming Help", "Reading Tutoring", "SAT Tutoring", "Science Tutoring", "Spanish Tutoring", "Statistics Tutoring", "Writing Tutoring"};

    public static  String[] Otherskillslabels = {"Academic Achievement", "Academic Advising", "Alumni Relations", "Answering Telephones", "Antiques", "Anything Goes",
            "Appointment Setting", "Art Installation", "Comics", "Computational Fluid Dynamics", "Counselling and Psychotherapy",
            "Development Assessment", "Development Consulting", "Fitness", "Freelance", "ISO/IEC 17025 Calibration", "Managed Care", "MIDI",
            "Podcasting", "Quantum Computing", "Social Impact", "TikTok", "Town Planning", "TS/ISO 16949 Audit", "Udacity", "US Taxation",
            "Video Game Coaching"};
}
