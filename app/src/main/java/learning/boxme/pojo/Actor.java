
package learning.boxme.pojo;


public class Actor {

    private Integer id;
    private String login;
    private String displayLogin;
    private String gravatarId;
    private String url;
    private String avatar_url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getDisplayLogin() {
        return displayLogin;
    }

    public void setDisplayLogin(String displayLogin) {
        this.displayLogin = displayLogin;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatar_url = avatarUrl;
    }

}
