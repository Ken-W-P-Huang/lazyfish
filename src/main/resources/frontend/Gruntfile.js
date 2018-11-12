/**
 * Created by kenhuang on 2018/10/23.
 */
module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
/***********************************************************************************************************************
 * css/js内容合并
 **********************************************************************************************************************/
        concat: {
            options: {
                stripBanners: true,
                banner: '/*!<%= pkg.name %> - <%= pkg.version %>-' + '<%=grunt.template.today("yyyy-mm-dd") %> */'
            },
            cssConcat: {
                // src:[],
                // dest:''
            },
            // jsConcat:{
            //     src:'',
            //     dest:''
            // }
        },
/***********************************************************************************************************************
 * 压缩css
 **********************************************************************************************************************/
        cssmin: {
            options: {
                stripBanners: true,
                banner: '/*!<%= pkg.name %> - <%= pkg.version %>-' + '<%=grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/css/',
                    src: '*.css',
                    dest: 'build/libs/exploded/<%= pkg.warName %>/css',
                }]
            }
        },
/***********************************************************************************************************************
 * 压缩js
 **********************************************************************************************************************/
        uglify: {
            options: {
                stripBanners: true,
                banner: '/*!<%= pkg.name %> - <%= pkg.version %>-' + '<%=grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                files: [{
                    expand: true,
                    cwd: 'src/main/webapp/js/',
                    src: '*.js',
                    dest: 'build/libs/exploded/<%= pkg.warName %>/js',
                }]
            }
        },
/***********************************************************************************************************************
 * js语法检查
 **********************************************************************************************************************/
        jshint: {
            options: {
//http://jshint.com/docs/
                "maxerr": 50,       // {int} Maximum error before stopping

                // Enforcing
                "bitwise": true,     //Prohibit bitwise operators (&, |, ^, etc.)
                "camelcase": false,    //Identifiers must be in camelCase
                "curly": true,     //Require {} for every new block or scope
                "eqeqeq": true,     //Require triple equals (===) for comparison
                "forin": true,     //Require filtering for in loops with obj.hasOwnProperty()
                "freeze": true,     //prohibits overwriting prototypes of native objects
                "immed": false,    //Require immediate invocations to be wrapped in parens
                "latedef": false,    //Require variables/functions to be defined before being used
                "newcap": false,    //Require capitalization of all constructor functions
                "noarg": true,     //Prohibit use of `arguments.caller` and `arguments.callee`
                "noempty": true,     //Prohibit use of empty blocks
                "nonbsp": true,     //Prohibit "non-breaking whitespace" characters.
                "nonew": false,    //Prohibit use of constructors for side-effects
                "plusplus": false,    //Prohibit use of `++` and `--`
                "quotmark": false,
                "undef": true,     //Require all non-global variables to be declared
                "unused": true,
                "strict": false,     //Requires all functions run in ES5 Strict Mode
                "maxparams": false,    // {int} Max number of formal params allowed per function
                "maxdepth": false,    // {int} Max depth of nested blocks (within functions)
                "maxstatements": false,    // {int} Max number statements per function
                "maxcomplexity": false,    // {int} Max cyclomatic complexity per function
                "maxlen": false,    // {int} Max number of characters per line
                "varstmt": false,

                // Relaxing
                "asi": true,     //Tolerate Automatic Semicolon Insertion (no semicolons)
                "boss": false,     //Tolerate assignments where comparisons would be expected
                "debug": false,     //Allow debugger statements e.g. browser breakpoints.
                "eqnull": false,     //Tolerate use of `== null`
                "esversion": 5,
                "moz": false,     //Allow Mozilla specific syntax
                "evil": false,     //Tolerate use of `eval` and `new Function()`
                "expr": false,     //Tolerate `ExpressionStatement` as Programs
                "funcscope": false,     //Tolerate defining variables inside control statements
                "globalstrict": false,     //Allow global "use strict" (also enables 'strict')
                "iterator": false,     //Tolerate using the `__iterator__` property
                "lastsemic": false,
                "laxbreak": false,     //Tolerate possibly unsafe line breakings
                "laxcomma": false,     //Tolerate comma-first style coding
                "loopfunc": false,     //Tolerate functions being defined in loops
                "multistr": false,     //Tolerate multi-line strings
                "noyield": false,     //Tolerate generator functions with no yield statement
                "notypeof": false,     //Tolerate invalid typeof operator values
                "proto": false,     //Tolerate using the `__proto__` property
                "scripturl": false,     //Tolerate script-targeted URLs
                "shadow": false,     //Allows re-define variables later in code
                "sub": false,
                "supernew": false,     //Tolerate `new function () { ... };` and `new Object;`
                "validthis": false,     //Tolerate using this in a non-constructor function

                // Environments
                "browser": true,     // Web Browser (window, document, etc)
                "browserify": false,    // Browserify (node.js code in the browser)
                "couch": false,    // CouchDB
                "devel": true,     // Development/debugging (alert, confirm, etc)
                "dojo": false,    // Dojo Toolkit
                "jasmine": false,    // Jasmine
                "jquery": true,    // jQuery
                "mocha": true,     // Mocha
                "mootools": false,    // MooTools
                "node": false,    // Node.js
                "nonstandard": false,    // Widely adopted globals (escape, unescape, etc)
                "phantom": false,    // PhantomJS
                "prototypejs": false,    // Prototype and Scriptaculous
                "qunit": false,    // QUnit
                "rhino": false,    // Rhino
                "shelljs": false,    // ShellJS
                "typed": false,    // Globals for typed array constructions
                "worker": false,    // Web Workers
                "wsh": false,    // Windows Scripting Host
                "yui": false,    // Yahoo User Interface

                // Custom Globals
                "globals": {
                    "jQuery": true,
                    "console": true,
                    "module": true
                }
            },
            build: ['Gruntfile.js', 'src/main/webapp/js/bcs.js']
        },
/***********************************************************************************************************************
 * css语法检查
 **********************************************************************************************************************/
        csslint: {
            options: {
                "adjoining-classes": false,
                "box-sizing": false,
                "box-model": false,
                "compatible-vendor-prefixes": false,
                "floats": false,
                "font-sizes": false,
                "gradients": false,
                "important": false,
                "known-properties": false,
                "outline-none": false,
                "qualified-headings": false,
                "regex-selectors": false,
                "shorthand": false,
                "text-indent": false,
                "unique-headings": false,
                "universal-selector": false,
                "unqualified-attributes": false
            },
            build: []
        },
/***********************************************************************************************************************
 * watch
 **********************************************************************************************************************/
        watch: {
            build: {
                files: ['src/main/webapp/js/*.js', 'src/main/webapp/css/*.css'],
                tasks: ['jshint', 'csslint', 'concat', 'cssmin', 'uglify'],
                options: {spawn: false}
            }
        }
    })
    grunt.loadNpmTasks('grunt-contrib-concat')
    grunt.loadNpmTasks('grunt-contrib-cssmin')
    grunt.loadNpmTasks('grunt-contrib-uglify')
    grunt.loadNpmTasks('grunt-contrib-jshint')
    grunt.loadNpmTasks('grunt-contrib-csslint')
    grunt.loadNpmTasks('grunt-contrib-watch')
    grunt.registerInitTask('default', ['jshint', 'csslint', 'concat', 'cssmin', 'uglify']) //,'watch'
};