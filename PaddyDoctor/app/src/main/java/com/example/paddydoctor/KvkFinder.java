package com.example.paddydoctor;

import static com.example.paddydoctor.MainActivity.lng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KvkFinder {
    private static final double EARTH_RADIUS = 6371; // in km
    private static final double RADIUS = 100; // in km
    public static String kvkaddress;
    public static double kvklat;
    public static double kvklong;
    public static String kvkcontact;

    public String KvkFinders(double lat, double lon) {
        List<Kvk> kvks = new ArrayList<>();
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Station Road, Indi, & Dist. Vijayapura, Karnataka 586209", "+91 835 9295705", 16.8269686, 75.7326474));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Near Railway Station, Old Badami Road, Bagalkot Dist., Bagalkot 587101", "+91 835 4223543", 16.171004, 75.6813028));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Hadonahalli Village, Doddaballapur Taluk, Bengaluru Rural District. 561205", "+91 94498 66928", 13.3749738, 77.5436184));
        kvks.add(new Kvk("ICAR-BIRDS Krishi Vigyan Kendra, AT/Post: Tukkanatti, Tq.: Mudalagi, Dist.: Belagavi 591224", "+91 94807 51345", 16.3100063, 74.8448737));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Mattikipp Village, Baihongal Taluk, Belgavi District 591147", "+91 95356 04747", 15.8924191, 74.7414433));
        kvks.add(new Kvk("Krishi Vigyan Kendra, At: Hagari, Tq/Dist: Ballari 583111", "+91 839 2265080", 15.1377954, 77.0559762));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Post Box No.58, Distt. Bidar 585402", "+91 848 2244155", 17.985883, 77.482601));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Regional Agricultural Research Station, P. Box No.18, PO. & Distt. Vijayapura 586102", "+91 835 2200140", 16.7683623, 75.7149252));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Seed Farm, Haradanahally, Distt. Chamrajanagar 571127", "+91 822 6297050", 11.8933906, 76.9505499));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Hand post, Mudigere tq., Distt. Chikkamagalur 577132", "+91 826 3228198", 13.1230218, 75.6260304));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, P.B. No-29, Kurubur Farm, Chintamani Taluk 563125", "+91 94498 66930", 13.3408892, 78.0817648));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Babbur Farm, Hiriyur Tq., Chitradurga District 577598", "+91 819 3200081", 13.958704, 76.6391192));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Kankanady, Mangaluru 575002", "+91 824 2431872", 12.8576623, 74.8606088));
        kvks.add(new Kvk("Kadalivana, LIC Colony Layout, BIET College Road, Davanagere 577004", "+91 819 2263462", 14.4410145, 75.9081542));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Saidapur Farm, Dist. Dharwad 580005", "+91 836 2970246", 15.4855654, 74.9731469));
        kvks.add(new Kvk("ICAR-K.H.Patil Krishi Vigyan Kendra, Hulkoti, Distt. Gadag 582205", "+91 837 2289069", 15.4230171, 75.5262665));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Agricultural Research Station, Aland Road, Dist. Kalaburagi, 585101", "+91 847 2274596", 17.3606862, 76.8125892));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Kalaburgi-II (Raddewadgi) Post-Raddewadgi, Taluk-Jewargi, Dist.-Kalaburagi, 585310", "+91 1234567893", 17.0399644, 76.7974889));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Kandail, Hassan, 573217", "+91 817 2256092", 12.982539, 76.0401495));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Hanumanmatti, Tq Ranebennur, Dt. Haveri, 581115", "+91 837 3253524", 14.6620363, 75.5572317));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Gonikoppal, Kodagu District, 571213", "+91 827 4247274", 12.1856301, 75.9222889));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Tamaka, Kolar-563103, 563103", "+91 815 2295098", 13.2365995, 78.0607812));
        kvks.add(new Kvk("ARS Campus, Kanakagiri Road, Gangavathi, 583227", "+91 853 3272518", 15.4528136, 76.5238845));
        kvks.add(new Kvk("Krishi Krishi Vigyan Kendra, VC Farm, Mandya Distt., 571405", "+91 823 2277456", 12.5685942, 76.8335149));
        kvks.add(new Kvk("JSS Krishi Vigyan Kendra, Suttur, Nanjangud Taluk, Mysuru district, 571129", "+91 822 1295018", 12.1486953, 76.7747083));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, UAS Campus, Raichur, 584104", "+91 853 220196", 16.2353802, 77.2038446));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Chandurayanghalli, Magadi Taluk, Ramanagara District, 562120", "+91 802 9899388", 12.9576487, 77.2239384));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, Savalanga Road, Navule, Shivamogga, 577204", "+91 123456789", 13.9684529, 75.5759817));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, NH4, Hirehalli, Tumakuru District, 572168", "+91 816 2243175", 13.2818418, 77.1838514));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Konehally, Tiptur, Distt. Tumkur, 572202", "+91 813 4298955", 13.2816962, 76.3563219));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Zonal Agricultural Research Station, Brahmavar, Distt. Udupi, 576213", "+91 820 42563923", 13.4249216, 74.7549209));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Banvasi Road, Sirsi, Distt. Uttara Kannada 581401", "+91 838 4228411", 14.6080082, 74.8275645));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Kellappaji College of Agriculture Engineering & Technology, Tavanur, Distt. Malappuram 679573", "+91 494 2686329", 10.8525991, 75.9838633));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Regional Agricultural Research Station, Kumarakom, Distt.-Kottayam 686563", "+91 481 2523421", 9.6254774, 76.4247118));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Kanhirangad P.O, Taliparamba, Kannur (Dist) 670142", "+91 460 2226087", 12.0812676, 75.3948922));
        kvks.add(new Kvk("Krishi Vigyan Kendra, KAU P.O, Thrissur 680656", "+91 487 2375855", 10.5468926, 76.2657837));
        kvks.add(new Kvk("ICAR-KVK-Alappuzha, ICAR-CPCRI, Regional Station, Kayamkulam, PO. Krishnapuram, Distt. Alappuzha 690533", "+91 479 2959268", 9.1494351, 76.5121303));
        kvks.add(new Kvk("Krishi Vigyan Kendra,Sadanandapuram,Distt. Kollam 691531", "+91 474 2663599", 8.981426, 76.8085822));
        kvks.add(new Kvk("Krishi Vigyan Kendra,Santhanpara,Distt. Idukki 685619", "+91 486 8299871", 9.9593236, 77.2192285));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, CARD, Kolabhagom P.O., Thadiyoor,Distt. Pathanamthitta 689545", "+91 469 2662094", 9.3787528, 76.6854209));
        kvks.add(new Kvk("ICAR-Krishi Vigyan Kendra, ICAR-CPCRI,PO. Kudlu,Distt. Kasaragod", "+91 499 4232993", 12.5316462, 74.964881));
        kvks.add(new Kvk("Krishi Vigyan Kendra,Peruvannamuzhi,Distt. Kozhikode (Calicut)", "+91 496 2966041", 11.6032279, 75.8222518));
        kvks.add(new Kvk("Krishi Vigyan Kendra, Ambalavayal, Wayanad district.", "+91 493 6260411", 11.6165604, 75.6837309));
        kvks.add(new Kvk("Krishi Vigyan Kendra,Pattambi,Dist. Palghat.", "+91 466 2212279", 10.8126765, 76.1880778));
        kvks.add(new Kvk("Krishi Vigyan Kendra,Mitraniketan, Velland, Thiruvananthapuram Distt", "+91 828 1114479", 8.5649397, 77.0614881));
        kvks.add(new Kvk("ICAR-KVK of CMFRI, Narakkal Post, Ernakulam district", "+91 484 2972450", 10.0423605, 76.2047486));
        kvks.add(new Kvk("KVK- Lakshadweep, Second floor, Agricultural Demonstration Unit,Secretariat Road Kavaratti -682555", "+91 489 6263710", 10.5579541, 72.6052434));
        kvks.add(new Kvk("Kawadimatti 585 224 Taluk: Shorapur Dist: Yadgir", "+91 987654321", 16.5204183, 76.7477493));

        List<KvkDistance> distances = new ArrayList<>();
        for (Kvk kvk : kvks) {
            double distance = getDistance(lat, lon, kvk.getLatitude(), kvk.getLongitude());
            if (distance <= RADIUS) {
                distances.add(new KvkDistance(kvk, distance));
            }
        }

        Collections.sort(distances, Comparator.comparingDouble(KvkDistance::getDistance));

        if (!distances.isEmpty()) {
            kvklat = distances.get(0).getKvk().getLatitude();
            kvklong= distances.get(0).getKvk().getLongitude();
            kvkcontact = distances.get(0).getKvk().getContactNumber();
            kvkaddress = distances.get(0).getKvk().getAddress();
            return "Nearest KVK"/* + "\n" + distances.get(0).getKvk().getAddress() + "\n" + kvkcontact*/;
        } else {
            kvklat = MainActivity.lat;
            kvklong = MainActivity.lng;
            kvkcontact = "";
            return "No KVK found within " + RADIUS + " km radius";
        }
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

}
class Kvk {
    private String address;
    private String contactNumber;
    private double latitude;
    private double longitude;

    public Kvk(String address, String contactNumber, double latitude, double longitude) {
        this.address = address;
        this.contactNumber = contactNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
class KvkDistance {
    private Kvk kvk;
    private double distance;
    public KvkDistance(Kvk kvk, double distance) {
        this.kvk = kvk;
        this.distance = distance;
    }

    public Kvk getKvk() {
        return kvk;
    }

    public double getDistance() {
        return distance;
    }
}
