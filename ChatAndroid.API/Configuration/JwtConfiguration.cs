namespace ChatAndroid.API.Configuration
{
    public class JwtConfiguration
    {
        public string Key     { get; set; }
        public int    Expires { get; set; }
        public string Issuer  { get; set; }
    }
}