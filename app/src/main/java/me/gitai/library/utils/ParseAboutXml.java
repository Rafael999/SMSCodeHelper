package me.gitai.library.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by gitai on 16-2-5.
 */
public class ParseAboutXml {
    private static final String S = "s";
    private static final String TAG_ROOT            = "about";

    private static final String TAG_NAME            = "name";
    private static final String TAG_ICON            = "icon";
    private static final String TAG_VERSION         = "ver";
    private static final String TAG_CODE            = "code";
    private static final String TAG_DESCRIPTION     = "des";
    private static final String TAG_LICENSE         = "license";
    private static final String TAG_REPOSITORY      = "rep";

    private static final String TAG_HOME            = "home";
    private static final String TAG_HOMES           = TAG_HOME + S;

    private static final String TAG_BUG             = "bug";
    private static final String TAG_BUGS            = TAG_BUG + S;

    private static final String TAG_AUTHOR          = "dever";
    private static final String TAG_AUTHORS         = TAG_AUTHOR + S;

    private static final String TAG_DEPENDENCIE     = "dep";
    private static final String TAG_DEPENDENCIES    = TAG_DEPENDENCIE + S;

    private static final String TAG_CHANGELOG       = "clog";
    private static final String TAG_CHANGELOGS      = TAG_CHANGELOG + S;

    private static final String ATTR_TITLE          = "title";
    private static final String ATTR_HREF           = "href";
    private static final String ATTR_NAME           = "name";
    private static final String ATTR_CODE           = "code";
    private static final String ATTR_TYPE           = "type";
    private static final String ATTR_PATH           = "path";
    private static final String ATTR_EMAIL          = "email";

    private static String currTag;

    public static About Parse(Context ctx, int id) throws IOException, XmlPullParserException {
        return Parse(ctx.getResources().getXml(id));
    }

    public static About Parse(XmlResourceParser parse)
            throws XmlPullParserException,IOException{
        About about = new About();

        int event = parse.getEventType();
        String text = null;
        LinkedHashMap<String,String> attrs = new LinkedHashMap<>();
        while (event != XmlPullParser.END_DOCUMENT){
            if (event == XmlPullParser.START_TAG){
                currTag = parse.getName();
                if (!StringUtils.contains(new String[]{
                        TAG_ROOT,
                        TAG_NAME,
                        TAG_CODE,
                        TAG_ICON,
                        TAG_VERSION,
                        TAG_DESCRIPTION,
                        TAG_LICENSE,
                        TAG_REPOSITORY,
                        TAG_HOME,
                        TAG_HOMES,
                        TAG_BUG,
                        TAG_BUGS,
                        TAG_AUTHOR,
                        TAG_AUTHORS,
                        TAG_DEPENDENCIE,
                        TAG_DEPENDENCIES,
                        TAG_CHANGELOG,
                        TAG_CHANGELOGS
                }, currTag)){
                    throw new XmlPullParserException(
                            String.format("Error in xml:Invalid tag \"%s\" at line(%s).",
                                    currTag,
                                    parse.getLineNumber()));
                }
                for (int i = 0; i < parse.getAttributeCount(); i++) {
                    attrs.put(
                            parse.getAttributeName(i),
                            parse.getAttributeValue(i));
                }
            }else if (event == XmlPullParser.TEXT){
                text = parse.getText();
            }else if(event == XmlPullParser.END_TAG){
                switch (currTag){
                    case TAG_NAME:
                        about.setName(text);
                        break;
                    case TAG_ICON:
                        about.setIcon(text);
                        break;
                    case TAG_VERSION:
                        about.setVersion(text);
                        break;
                    case TAG_CODE:
                        about.setCode(text);
                        break;
                    case TAG_DESCRIPTION:
                        about.setDescription(text);
                        break;
                    case TAG_LICENSE:
                        if (attrs.isEmpty())break;
                        about.setLicense(
                                new Url(text,
                                        attrs.get(ATTR_HREF)));
                        break;
                    case TAG_REPOSITORY:
                        if (attrs.isEmpty())break;
                        about.setRepository(
                                new Repository(
                                        attrs.get(ATTR_TYPE),
                                        text,
                                        attrs.get( ATTR_HREF)));
                        break;
                    case TAG_HOME:
                        if (attrs.isEmpty())break;
                        about.getHome()
                                .add(new Url(text,
                                        attrs.get(ATTR_HREF)));
                        break;
                    case TAG_BUG:
                        if (attrs.isEmpty())break;
                        about.getBugs()
                                .add(new Url(text,
                                        attrs.get(ATTR_HREF)));
                        break;
                    case TAG_AUTHOR:
                        if (attrs.isEmpty())break;
                        about.getAuthors()
                                .add(new Author(text,
                                        attrs.get(ATTR_HREF),
                                        attrs.get(ATTR_EMAIL)));
                        break;
                    case TAG_DEPENDENCIE:
                        if (attrs.isEmpty())break;
                        about.getDependencies()
                                .add(new Dependencie(attrs.get(ATTR_NAME),
                                        attrs.get(ATTR_HREF),
                                        attrs.get(ATTR_TITLE),
                                        attrs.get(ATTR_CODE),
                                        attrs.get(ATTR_TYPE),
                                        attrs.get(ATTR_PATH),
                                        new License(
                                                text,
                                                attrs.get(ATTR_TYPE))));
                        break;
                    case TAG_CHANGELOG:
                        if (attrs.isEmpty())break;
                        about.getChangelogs()
                                .add(new ChangeLog(attrs.get(ATTR_NAME),
                                        attrs.get(ATTR_HREF),
                                        attrs.get(ATTR_TYPE),
                                        attrs.get(ATTR_CODE),
                                        text));
                        break;
                }
                text = null;
                attrs.clear();
            }
            event = parse.next();
        }
        parse.close();
        return about;
    }

    public static class About{
        private String name;
        private String icon;
        private String version;
        private String code;
        private String description;
        private List<Url> home = new ArrayList<>();
        private List<Url> bugs = new ArrayList<>();
        private Url license;
        private Repository repository;
        private List<Author> authors = new ArrayList<>();
        private List<Dependencie> dependencies = new ArrayList<>();
        private List<ChangeLog> changelogs = new ArrayList<>();

        public About(){

        }

        public String getName() {
            return name;
        }

        public String getName(String defaultValue) {
            if (StringUtils.isEmpty(name)){
                return defaultValue;
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getVersion() {
            return version;
        }

        public String getVersion(String defaultValue) {
            if (StringUtils.isEmpty(version)){
                return defaultValue;
            }
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCode() {
            return code;
        }

        public String getCode(String defaultValue) {
            if (StringUtils.isEmpty(code)){
                return defaultValue;
            }
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public String getDescription(String defaultValue) {
            if (StringUtils.isEmpty(description)){
                return defaultValue;
            }
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Url> getHome() {
            return home;
        }

        public List<Url> getHome(List<Url> defaultValue) {
            if (StringUtils.isEmpty(home)){
                return defaultValue;
            }
            return home;
        }

        public void setHome(List<Url> home) {
            this.home = home;
        }

        public List<Url> getBugs() {
            return bugs;
        }

        public List<Url> getBugs(List<Url> defaultValue) {
            if (StringUtils.isEmpty(bugs)){
                return defaultValue;
            }
            return bugs;
        }

        public void setBugs(List<Url> bugs) {
            this.bugs = bugs;
        }

        public Url getLicense() {
            return license;
        }

        public Url getLicense(Url defaultValue) {
            if (Url.isEmpty(license)){
                return defaultValue;
            }
            return license;
        }

        public void setLicense(Url license) {
            this.license = license;
        }

        public Repository getRepository() {
            return repository;
        }

        public Repository getRepository(Repository defaultValue) {
            if (Repository.isEmpty(repository)){
                return defaultValue;
            }
            return repository;
        }

        public void setRepository(Repository repository) {
            this.repository = repository;
        }

        public List<Author> getAuthors() {
            return authors;
        }

        public List<Author> getAuthors(List<Author> defaultValue) {
            if (StringUtils.isEmpty(authors)){
                return defaultValue;
            }
            return authors;
        }

        public void setAuthors(List<Author> authors) {
            this.authors = authors;
        }

        public List<Dependencie> getDependencies() {
            return dependencies;
        }

        public List<Dependencie> getDependencies(List<Dependencie> defaultValue) {
            if (StringUtils.isEmpty(dependencies)){
                return defaultValue;
            }
            return dependencies;
        }

        public void setDependencies(List<Dependencie> dependencies) {
            this.dependencies = dependencies;
        }

        public List<ChangeLog> getChangelogs() {
            return changelogs;
        }

        public List<ChangeLog> getChangelogs(List<ChangeLog> defaultValue) {
            if (StringUtils.isEmpty(changelogs)){
                return defaultValue;
            }
            return changelogs;
        }

        public void setChangelogs(List<ChangeLog> changelogs) {
            this.changelogs = changelogs;
        }
    }

    public static class Url{
        private String href;
        private String name;

        public Url(String name, String href) {
            this.href = href;
            this.name = name;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static boolean isEmpty(Url url){
            return url == null || (url.href == null && url.name == null);
        }
    }

    public static class Repository extends Url{
        public static final String TYPE_GIT = "git";
        public static final String TYPE_SVN = "svn";

        public static final int INT_TYPE_GIT = 0;
        public static final int INT_TYPE_SVN = 1;

        private int type = -1;

        public Repository(String type, String name, String href) {
            super(href, name);
            switch (type){
                case TYPE_GIT:
                    this.type = INT_TYPE_GIT;
                case TYPE_SVN:
                    this.type = INT_TYPE_SVN;
            }
        }

        public Repository(String name, String href) {
            super(href, name);
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static boolean isEmpty(Repository rep){
            return rep == null || (rep.getHref() == null && rep.getName() == null && rep.getType() == -1);
        }
    }

    public static class Author extends Url{
        private String email;

        public Author(String name, String href, String email) {
            super(name, href);
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Dependencie extends Url{
        private String title;
        private String code;
        private String type;
        private String path;
        private License license;

        public Dependencie(String name, String href, String title, String code, String type, String path, License license) {
            super(name, href);
            this.title = title;
            this.code = code;
            this.type = type;
            this.path = path;
            this.license = license;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public License getLicense() {
            return license;
        }

        public void setLicense(License license) {
            this.license = license;
        }
    }

    public static class License{
        public static final String TYPE_MD= "md";
        public static final String TYPE_HTML = "html";
        public static final String TYPE_TXT = "txt";

        public static final int INT_TYPE_MD = 0;
        public static final int INT_TYPE_HTML = 1;
        public static final int INT_TYPE_TXT = 2;

        private int type;
        private String text;

        public License(String text, String type) {
            this.text = text;
            if (StringUtils.isEmpty(type)){
                this.type = INT_TYPE_TXT;
                return;
            }
            switch (type){
                case TYPE_MD:
                    this.type = INT_TYPE_MD;
                case TYPE_HTML:
                    this.type = INT_TYPE_HTML;
                case TYPE_TXT:
                    this.type = INT_TYPE_TXT;
            }
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            switch(type){
                case INT_TYPE_MD:
                    return text;
                case INT_TYPE_HTML:
                    return text;
                case INT_TYPE_TXT:
                    return text;
                default:
                    return "";
            }
        }

        public Spanned toSpanned() {
            if (StringUtils.isEmpty(text)){
                return null;
            }
            switch(type){
                case INT_TYPE_MD:
                    return new SpannableString(text);
                case INT_TYPE_HTML:
                    return Html.fromHtml(text);
                case INT_TYPE_TXT:
                    return new SpannableString(text);
                default:
                    return null;
            }
        }
    }

    public static class ChangeLog extends Url{
        public static final String TYPE_MD= "md";
        public static final String TYPE_HTML = "html";
        public static final String TYPE_TXT = "txt";

        private String type;
        private String code;
        private String content;

        public ChangeLog(String name, String href, String type, String code, String content) {
            super(name, href);
            this.code = code;
            if (StringUtils.isEmpty(type)){
                this.type = TYPE_TXT;
            }else{
                this.type = type;
            }
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Spanned getContent() {
            return toSpanned();
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Spanned toSpanned() {
            if (StringUtils.isEmpty(content)){
                return null;
            }
            switch(type){
                case TYPE_MD:
                    return new SpannableString(content);
                case TYPE_HTML:
                    return Html.fromHtml(content);
                case TYPE_TXT:
                    return new SpannableString(content);
                default:
                    return null;
            }
        }
    }
}
