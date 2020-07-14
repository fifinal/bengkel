package com.example.bengkel.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.bengkel.R;
import com.example.bengkel.algoritma.Edge;
import com.example.bengkel.algoritma.Node;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {
    Map<String, Node> map;
    List<Edge> edgeList;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        firebaseFirestore=FirebaseFirestore.getInstance();

//        node();
        edge();
    }

    private void node(){
        map=new HashMap<>();

        map.put("0",new Node("0",new GeoPoint(-7.39382888026373, 112.63131553637656)));
        map.put("1",new Node("1",new GeoPoint(-7.397744245303545,112.6307469080654)));
        map.put("2",new Node("2",new GeoPoint(-7.399350811668749,112.63778636918187)));
        map.put("3",new Node("3",new GeoPoint(-7.3941480689312336,112.63852665886998)));
        map.put("4",new Node("4",new GeoPoint(-7.401177604323857,112.65652497266755)));
        map.put("5",new Node("5",new GeoPoint(-7.390795289301357,112.65863346512288)));
        map.put("6",new Node("6",new GeoPoint(-7.3903954431817285, 112.6678323469003)));
        map.put("7",new Node("7",new GeoPoint(-7.386054420659774,112.67740246866543)));
        map.put("8",new Node("8",new GeoPoint(-7.392601664885367,112.67189164896546)));
        map.put("9",new Node("9",new GeoPoint(-7.389814066303561,112.67572184343872)));
        map.put("10",new Node("10",new GeoPoint(-7.394110307670756, 112.67386067768741)));
        map.put("11",new Node("11",new GeoPoint(-7.406160896442139,112.67279430346964)));
        map.put("12",new Node("12",new GeoPoint(-7.4046151537143015, 112.66644538151999)));
        map.put("13",new Node("13",new GeoPoint(-7.4193370805415, 112.6623262483934)));
        map.put("14",new Node("14",new GeoPoint(-7.420720149714339,112.67296925376449)));
        map.put("15",new Node("15",new GeoPoint(-7.413242534499598,112.67411856483085)));
        map.put("16",new Node("16",new GeoPoint(-7.422512892964231,112.68353217790617)));
        map.put("17",new Node("17",new GeoPoint(-7.407079579878411,112.68334431582876)));
        map.put("19",new Node("19",new GeoPoint(-7.3965523715148445, 112.68683407952739)));
        map.put("20",new Node("20",new GeoPoint(-7.396749249057729,112.6939729227097)));
        map.put("21",new Node("21",new GeoPoint(-7.393238181710282,112.69489560261083)));
        map.put("22",new Node("22",new GeoPoint(-7.387373026448214, 112.6860779766235)));
        map.put("23",new Node("23",new GeoPoint(-7.359153640825903,112.68099572644356)));

        for (String key:map.keySet()){
            firebaseFirestore.collection("Nodes")
                    .document(key).set(map.get(key))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GraphActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void edge(){
        edgeList=new ArrayList<>();

        edgeList.add(new Edge("0-1",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.39382888026373, 112.63131553637656),
                new GeoPoint(-7.394062951975795, 112.63135886933162),
                new GeoPoint(-7.394898161391524, 112.63109064843013),
                new GeoPoint(-7.395552496326708, 112.63096190239742),
                new GeoPoint(-7.395578490699596, 112.63103663921356),
                new GeoPoint(-7.396508256226472, 112.63095117356136),
                new GeoPoint(-7.397024274658464, 112.63082242752864),
                new GeoPoint(-7.397744245303545, 112.6307469080654)
        )),(float)450.70));

        edgeList.add(new Edge("1-0",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.397744245303545,112.6307469080654),
                new GeoPoint(-7.397024274658464,112.63082242752864),
                new GeoPoint(-7.396508256226472,112.63095117356136),
                new GeoPoint(-7.395578490699596,112.63103663921356),
                new GeoPoint(-7.395552496326708,112.63096190239742),
                new GeoPoint(-7.394898161391524,112.63109064843013),
                new GeoPoint(-7.394062951975795,112.63135886933162),
                new GeoPoint(-7.39382888026373,112.63131553637656)
        )),(float)450.70));


        edgeList.add(new Edge("0-3",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.39382888026373, 112.63131553637656),
                new GeoPoint(-7.393816554865955, 112.63222745441557),
                new GeoPoint(-7.39434853586511, 112.6333486177838),
                new GeoPoint(-7.3949709528204, 112.63477018856169),
                new GeoPoint(-7.39490179542426, 112.63648596864685),
                new GeoPoint(-7.394279378371478, 112.63800277072747),
                new GeoPoint(-7.3941480689312336, 112.63852665886998)
        )),(float)839.8));

        edgeList.add(new Edge("3-0",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3941480689312336,112.63852665886998),
                new GeoPoint(-7.394279378371478,112.63800277072747),
                new GeoPoint(-7.39490179542426,112.63648596864685),
                new GeoPoint(-7.3949709528204,112.63477018856169),
                new GeoPoint(-7.39434853586511,112.6333486177838),
                new GeoPoint(-7.393816554865955,112.63222745441557),
                new GeoPoint(-7.39382888026373,112.63131553637656)
        )),(float)839.8));


        edgeList.add(new Edge("3-2",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3941480689312336, 112.63852665886998),
                new GeoPoint(-7.394344258712157, 112.6385767276463),
                new GeoPoint(-7.39478580245892, 112.63850699021191),
                new GeoPoint(-7.395376300032444, 112.63834069325299),
                new GeoPoint(-7.395722086533112, 112.63824413372845),
                new GeoPoint(-7.396769071608886, 112.6380724723515),
                new GeoPoint(-7.3981654191984365, 112.63782570912213),
                new GeoPoint(-7.399350811668749, 112.63778636918187)
        )),(float)588.3));

        edgeList.add(new Edge("2-3",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.399350811668749,112.63778636918187),
                new GeoPoint(-7.3981654191984365,112.63782570912213),
                new GeoPoint(-7.396769071608886,112.6380724723515),
                new GeoPoint(-7.395722086533112,112.63824413372845),
                new GeoPoint(-7.395376300032444,112.63834069325299),
                new GeoPoint(-7.39478580245892,112.63850699021191),
                new GeoPoint(-7.394344258712157,112.6385767276463),
                new GeoPoint(-7.3941480689312336,112.63852665886998)
        )),(float)588.3));


        edgeList.add(new Edge("1-2",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.397744245303545, 112.6307469080654),
                new GeoPoint(-7.39770473484215, 112.63092215457495),
                new GeoPoint(-7.397800490625827, 112.63126547732885),
                new GeoPoint(-7.398013281181825, 112.63160880008276),
                new GeoPoint(-7.398375024891515, 112.63198430934484),
                new GeoPoint(-7.398452161379484, 112.6320835510784),
                new GeoPoint(-7.3985904750482785, 112.63268704810675),
                new GeoPoint(-7.398947860640446, 112.63522926852748),
                new GeoPoint(-7.399051595779567, 112.63577912137553),
                new GeoPoint(-7.3992396677549275, 112.63683085320689),
                new GeoPoint(-7.399350811668749, 112.63778636918187)
        )),(float)813.4));

        edgeList.add(new Edge("2-1",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.399350811668749,112.63778636918187),
                new GeoPoint(-7.3992396677549275,112.63683085320689),
                new GeoPoint(-7.399051595779567,112.63577912137553),
                new GeoPoint(-7.398947860640446,112.63522926852748),
                new GeoPoint(-7.3985904750482785,112.63268704810675),
                new GeoPoint(-7.398452161379484,112.6320835510784),
                new GeoPoint(-7.398375024891515,112.63198430934484),
                new GeoPoint(-7.398013281181825,112.63160880008276),
                new GeoPoint(-7.397800490625827,112.63126547732885),
                new GeoPoint(-7.39770473484215,112.63092215457495),
                new GeoPoint(-7.397744245303545,112.6307469080654)
        )),(float)813.4));


        edgeList.add(new Edge("2-4",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.399350811668749, 112.63778636918187),
                new GeoPoint(-7.399556192637339, 112.63900159434169),
                new GeoPoint(-7.399598750587406, 112.63940392569393),
                new GeoPoint(-7.39969450596004, 112.64209149912685),
                new GeoPoint(-7.399928574561216, 112.64372764662593),
                new GeoPoint(-7.399934997731802, 112.64387678443575),
                new GeoPoint(-7.399879140464074, 112.6443917685666),
                new GeoPoint(-7.39974082719926, 112.64514546929979),
                new GeoPoint(-7.399665954879837, 112.64534435202),
                new GeoPoint(-7.399336130709696, 112.64574400116322),
                new GeoPoint(-7.3994315459081035, 112.64686578821052),
                new GeoPoint(-7.39922673565814, 112.64806554657106),
                new GeoPoint(-7.399306531870726, 112.64865295034532),
                new GeoPoint(-7.399639688348718, 112.65058025950722),
                new GeoPoint(-7.399912737509887, 112.65198656879097),
                new GeoPoint(-7.4000164724221476, 112.65228429399163),
                new GeoPoint(-7.400544211932876, 112.65340545735985),
                new GeoPoint(-7.400544211421749, 112.65367367723049),
                new GeoPoint(-7.400493673963902, 112.65423694112361),
                new GeoPoint(-7.400503228825199, 112.65436071336302),
                new GeoPoint(-7.400612283329905, 112.65506881654295),
                new GeoPoint(-7.400713358212635, 112.65546041905912),
                new GeoPoint(-7.400872950085585, 112.65584397494825),
                new GeoPoint(-7.401083079296908, 112.65629995048079),
                new GeoPoint(-7.401177604323857, 112.65652497266755)
        )),(float)2127.));

        edgeList.add(new Edge("4-2",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.401177604323857,112.65652497266755),
                new GeoPoint(-7.401083079296908,112.65629995048079),
                new GeoPoint(-7.400872950085585,112.65584397494825),
                new GeoPoint(-7.400713358212635,112.65546041905912),
                new GeoPoint(-7.400612283329905,112.65506881654295),
                new GeoPoint(-7.400503228825199,112.65436071336302),
                new GeoPoint(-7.400493673963902,112.65423694112361),
                new GeoPoint(-7.400544211421749,112.65367367723049),
                new GeoPoint(-7.400544211932876,112.65340545735985),
                new GeoPoint(-7.4000164724221476,112.65228429399163),
                new GeoPoint(-7.399912737509887,112.65198656879097),
                new GeoPoint(-7.399639688348718,112.65058025950722),
                new GeoPoint(-7.399306531870726,112.64865295034532),
                new GeoPoint(-7.39922673565814,112.64806554657106),
                new GeoPoint(-7.3994315459081035,112.64686578821052),
                new GeoPoint(-7.399336130709696,112.64574400116322),
                new GeoPoint(-7.399665954879837,112.64534435202),
                new GeoPoint(-7.39974082719926,112.64514546929979),
                new GeoPoint(-7.399879140464074,112.6443917685666),
                new GeoPoint(-7.399934997731802,112.64387678443575),
                new GeoPoint(-7.399928574561216,112.64372764662593),
                new GeoPoint(-7.39969450596004,112.64209149912685),
                new GeoPoint(-7.399598750587406,112.63940392569393),
                new GeoPoint(-7.399556192637339,112.63900159434169),
                new GeoPoint(-7.399350811668749,112.63778636918187)
        )),(float)2127.));


        edgeList.add(new Edge("4-5",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.401177604323857, 112.65652497266755),
                new GeoPoint(-7.400421188417187, 112.65663522660765),
                new GeoPoint(-7.397784794797398, 112.65719016629721),
                new GeoPoint(-7.396391014069072, 112.65752276021506),
                new GeoPoint(-7.3954813303802105, 112.65780707437064),
                new GeoPoint(-7.392869304223395, 112.65845080453421),
                new GeoPoint(-7.392656511187773, 112.65847762662436),
                new GeoPoint(-7.391145677686482, 112.65862246591117),
                new GeoPoint(-7.390795289301357, 112.65863346512288)
        )),(float)1181.5));

        edgeList.add(new Edge("5-4",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.390795289301357,112.65863346512288),
                new GeoPoint(-7.391145677686482,112.65862246591117),
                new GeoPoint(-7.392656511187773,112.65847762662436),
                new GeoPoint(-7.392869304223395,112.65845080453421),
                new GeoPoint(-7.3954813303802105,112.65780707437064),
                new GeoPoint(-7.396391014069072,112.65752276021506),
                new GeoPoint(-7.397784794797398,112.65719016629721),
                new GeoPoint(-7.400421188417187,112.65663522660765),
                new GeoPoint(-7.401177604323857,112.65652497266755)
        )),(float)1181.5));


        edgeList.add(new Edge("3-5",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3941480689312336, 112.63852665886998),
                new GeoPoint(-7.393313742454087, 112.64012511402937),
                new GeoPoint(-7.39317542713181, 112.64082785279128),
                new GeoPoint(-7.393159467668756, 112.64173980385634),
                new GeoPoint(-7.393069030700567, 112.6421099487004),
                new GeoPoint(-7.39279239985938, 112.64247472912642),
                new GeoPoint(-7.392249777321392, 112.6431023660359),
                new GeoPoint(-7.391928021501505, 112.6442181649861),
                new GeoPoint(-7.391358798688245, 112.64544661671492),
                new GeoPoint(-7.391316544537614, 112.64605616746695),
                new GeoPoint(-7.391107928620253, 112.6468202179221),
                new GeoPoint(-7.3897034870894895, 112.65052703078068),
                new GeoPoint(-7.389608823006513, 112.65070626180461),
                new GeoPoint(-7.389555624364506, 112.65092620294384),
                new GeoPoint(-7.3897045805459705, 112.65233168046764),
                new GeoPoint(-7.389827277610964, 112.65260309821153),
                new GeoPoint(-7.390859329487728, 112.6537510836699),
                new GeoPoint(-7.391189159991422, 112.65427679663682),
                new GeoPoint(-7.391651986250669, 112.6549741709807),
                new GeoPoint(-7.3916732656072535, 112.65530676489854),
                new GeoPoint(-7.391614747374188, 112.65555352812791),
                new GeoPoint(-7.391417913260632, 112.65593440180803),
                new GeoPoint(-7.390984068234243, 112.65674867331033),
                new GeoPoint(-7.390590399357306, 112.65775718389993),
                new GeoPoint(-7.390510601569214, 112.65808441339975),
                new GeoPoint(-7.390784573914777, 112.6581112354899),
                new GeoPoint(-7.390795289301357, 112.65863346512288)
        )),(float)2463.));

        edgeList.add(new Edge("5-3",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.390795289301357,112.65863346512288),
                new GeoPoint(-7.390784573914777,112.6581112354899),
                new GeoPoint(-7.390510601569214,112.65808441339975),
                new GeoPoint(-7.390590399357306,112.65775718389993),
                new GeoPoint(-7.390984068234243,112.65674867331033),
                new GeoPoint(-7.391417913260632,112.65593440180803),
                new GeoPoint(-7.391614747374188,112.65555352812791),
                new GeoPoint(-7.3916732656072535,112.65530676489854),
                new GeoPoint(-7.391651986250669,112.6549741709807),
                new GeoPoint(-7.391189159991422,112.65427679663682),
                new GeoPoint(-7.390859329487728,112.6537510836699),
                new GeoPoint(-7.389827277610964,112.65260309821153),
                new GeoPoint(-7.3897045805459705,112.65233168046764),
                new GeoPoint(-7.389555624364506,112.65092620294384),
                new GeoPoint(-7.389608823006513,112.65070626180461),
                new GeoPoint(-7.3897034870894895,112.65052703078068),
                new GeoPoint(-7.391107928620253,112.6468202179221),
                new GeoPoint(-7.391316544537614,112.64605616746695),
                new GeoPoint(-7.391358798688245,112.64544661671492),
                new GeoPoint(-7.391928021501505,112.6442181649861),
                new GeoPoint(-7.392249777321392,112.6431023660359),
                new GeoPoint(-7.39279239985938,112.64247472912642),
                new GeoPoint(-7.393069030700567,112.6421099487004),
                new GeoPoint(-7.393159467668756,112.64173980385634),
                new GeoPoint(-7.39317542713181,112.64082785279128),
                new GeoPoint(-7.393313742454087,112.64012511402937),
                new GeoPoint(-7.3941480689312336,112.63852665886998)
        )),(float)2463.));


        edgeList.add(new Edge("5-6",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.390795289301357, 112.65863346512288),
                new GeoPoint(-7.390460062962628, 112.65845992266183),
                new GeoPoint(-7.390369625441684, 112.65894808470254),
                new GeoPoint(-7.390261127193587, 112.66007898510122),
                new GeoPoint(-7.39028240661718, 112.66080318153524),
                new GeoPoint(-7.390266447049587, 112.66115723312521),
                new GeoPoint(-7.390176009489002, 112.6613181656661),
                new GeoPoint(-7.389356750743696, 112.66244469345236),
                new GeoPoint(-7.388867322716402, 112.66287384689474),
                new GeoPoint(-7.388659847628167, 112.66311691452425),
                new GeoPoint(-7.388611968747813, 112.66361580540102),
                new GeoPoint(-7.388654527752816, 112.66430245090883),
                new GeoPoint(-7.3887609252474205, 112.6646028583185),
                new GeoPoint(-7.389495067261847, 112.66592786957186),
                new GeoPoint(-7.3903954431817285, 112.6678323469003)
        )),(float)1190.5));

        edgeList.add(new Edge("6-5",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3903954431817285,112.6678323469003),
                new GeoPoint(-7.389495067261847,112.66592786957186),
                new GeoPoint(-7.3887609252474205,112.6646028583185),
                new GeoPoint(-7.388654527752816,112.66430245090883),
                new GeoPoint(-7.388611968747813,112.66361580540102),
                new GeoPoint(-7.388659847628167,112.66311691452425),
                new GeoPoint(-7.388867322716402,112.66287384689474),
                new GeoPoint(-7.389356750743696,112.66244469345236),
                new GeoPoint(-7.390176009489002,112.6613181656661),
                new GeoPoint(-7.390266447049587,112.66115723312521),
                new GeoPoint(-7.39028240661718,112.66080318153524),
                new GeoPoint(-7.390261127193587,112.66007898510122),
                new GeoPoint(-7.390369625441684,112.65894808470254),
                new GeoPoint(-7.390460062962628,112.65845992266183),
                new GeoPoint(-7.390795289301357,112.65863346512288)
        )),(float)1190.5));


        edgeList.add(new Edge("6-8",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3903954431817285, 112.6678323469003),
                new GeoPoint(-7.390882271072734, 112.66863282581014),
                new GeoPoint(-7.391573850841999, 112.66929801364583),
                new GeoPoint(-7.3917440856960495, 112.66984518428487),
                new GeoPoint(-7.392116474210506, 112.67057474513692),
                new GeoPoint(-7.3922015915411485, 112.67108972926778),
                new GeoPoint(-7.392601664885367, 112.67189164896546)
        )),(float)520.6));

        edgeList.add(new Edge("8-6",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.392601664885367,112.67189164896546),
                new GeoPoint(-7.3922015915411485,112.67108972926778),
                new GeoPoint(-7.392116474210506,112.67057474513692),
                new GeoPoint(-7.3917440856960495,112.66984518428487),
                new GeoPoint(-7.391573850841999,112.66929801364583),
                new GeoPoint(-7.390882271072734,112.66863282581014),
                new GeoPoint(-7.3903954431817285,112.6678323469003)
        )),(float)520.6));


        edgeList.add(new Edge("8-10",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.392601664885367, 112.67189164896546),
                new GeoPoint(-7.393867782094499, 112.67330187663734),
                new GeoPoint(-7.394110307670756, 112.67386067768741)
        )),(float)277.34));

        edgeList.add(new Edge("10-8",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.394110307670756,112.67386067768741),
                new GeoPoint(-7.393867782094499,112.67330187663734),
                new GeoPoint(-7.392601664885367,112.67189164896546)
        )),(float)277.34));


        edgeList.add(new Edge("10-11",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.394110307670756, 112.67386067768741),
                new GeoPoint(-7.394805137281407, 112.67367073297126),
                new GeoPoint(-7.39718840182316, 112.67299481629951),
                new GeoPoint(-7.398254041857527, 112.6727587819062),
                new GeoPoint(-7.401147231868522, 112.67292488984464),
                new GeoPoint(-7.403179362071201, 112.67294634751676),
                new GeoPoint(-7.404988826409859, 112.67267812661527),
                new GeoPoint(-7.405403761136354, 112.67264594010709),
                new GeoPoint(-7.406160896442139, 112.67279430346964)
        )),(float)1361.7));

        edgeList.add(new Edge("11-10",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.406160896442139,112.67279430346964),
                new GeoPoint(-7.405403761136354,112.67264594010709),
                new GeoPoint(-7.404988826409859,112.67267812661527),
                new GeoPoint(-7.403179362071201,112.67294634751676),
                new GeoPoint(-7.401147231868522,112.67292488984464),
                new GeoPoint(-7.398254041857527,112.6727587819062),
                new GeoPoint(-7.39718840182316,112.67299481629951),
                new GeoPoint(-7.394805137281407,112.67367073297126),
                new GeoPoint(-7.394110307670756,112.67386067768741)
        )),(float)1361.7));


        edgeList.add(new Edge("4-12",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.401177604323857, 112.65652497266755),
                new GeoPoint(-7.401302549731047, 112.6573383808136),
                new GeoPoint(-7.40125451048486, 112.65721445108188),
                new GeoPoint(-7.4014247416050924, 112.65794401193394),
                new GeoPoint(-7.401701367035354, 112.65884523416294),
                new GeoPoint(-7.40186095855081, 112.6598644735886),
                new GeoPoint(-7.402009910579806, 112.66153817201389),
                new GeoPoint(-7.402552521117142, 112.66255741143955),
                new GeoPoint(-7.402754669970164, 112.66386632943882),
                new GeoPoint(-7.402946179324386, 112.66442422891392),
                new GeoPoint(-7.403063212777694, 112.66487484002842),
                new GeoPoint(-7.403316306697866, 112.6653108053214),
                new GeoPoint(-7.403784439931777, 112.6658150606162),
                new GeoPoint(-7.404444081372329, 112.6662656717307),
                new GeoPoint(-7.4046151537143015, 112.66644538151999)
        )),(float)1230.5));

        edgeList.add(new Edge("12-4",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.4046151537143015,112.66644538151999),
                new GeoPoint(-7.404444081372329,112.6662656717307),
                new GeoPoint(-7.403784439931777,112.6658150606162),
                new GeoPoint(-7.403316306697866,112.6653108053214),
                new GeoPoint(-7.403063212777694,112.66487484002842),
                new GeoPoint(-7.402946179324386,112.66442422891392),
                new GeoPoint(-7.402754669970164,112.66386632943882),
                new GeoPoint(-7.402552521117142,112.66255741143955),
                new GeoPoint(-7.402009910579806,112.66153817201389),
                new GeoPoint(-7.40186095855081,112.6598644735886),
                new GeoPoint(-7.401701367035354,112.65884523416294),
                new GeoPoint(-7.4014247416050924,112.65794401193394),
                new GeoPoint(-7.40125451048486,112.65721445108188),
                new GeoPoint(-7.401302549731047,112.6573383808136),
                new GeoPoint(-7.401177604323857,112.65652497266755)
        )),(float)1230.5));


        edgeList.add(new Edge("12-11",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.4046151537143015, 112.66644538151999),
                new GeoPoint(-7.404724764993589, 112.66668384970487),
                new GeoPoint(-7.404937552209051, 112.66772454680265),
                new GeoPoint(-7.405256732839675, 112.66921585501493),
                new GeoPoint(-7.405575913239211, 112.6707853832256),
                new GeoPoint(-7.405884506830148, 112.67137010479085),
                new GeoPoint(-7.405996219837183, 112.67168124103658),
                new GeoPoint(-7.406102613150885, 112.67224986934774),
                new GeoPoint(-7.406139850804609, 112.67251809024923),
                new GeoPoint(-7.406160896442139, 112.67279430346964)
        )),(float)725.6));

        edgeList.add(new Edge("11-12",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.406160896442139,112.67279430346964),
                new GeoPoint(-7.406139850804609,112.67251809024923),
                new GeoPoint(-7.406102613150885,112.67224986934774),
                new GeoPoint(-7.405996219837183,112.67168124103658),
                new GeoPoint(-7.405884506830148,112.67137010479085),
                new GeoPoint(-7.405575913239211,112.6707853832256),
                new GeoPoint(-7.405256732839675,112.66921585501493),
                new GeoPoint(-7.404937552209051,112.66772454680265),
                new GeoPoint(-7.404724764993589,112.66668384970487),
                new GeoPoint(-7.4046151537143015,112.66644538151999)
        )),(float)725.6));


        edgeList.add(new Edge("11-15",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.406160896442139, 112.67279430346964),
                new GeoPoint(-7.4070631683380315, 112.67275565191528),
                new GeoPoint(-7.409377057859914, 112.67279818768807),
                new GeoPoint(-7.410403744388341, 112.67279818768807),
                new GeoPoint(-7.410982862240957, 112.672822979753),
                new GeoPoint(-7.411716967274642, 112.67320385343311),
                new GeoPoint(-7.413242534499598, 112.67411856483085)
        )),(float)826.4));

        edgeList.add(new Edge("15-11",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.413242534499598,112.67411856483085),
                new GeoPoint(-7.411716967274642,112.67320385343311),
                new GeoPoint(-7.410982862240957,112.672822979753),
                new GeoPoint(-7.410403744388341,112.67279818768807),
                new GeoPoint(-7.409377057859914,112.67279818768807),
                new GeoPoint(-7.4070631683380315,112.67275565191528),
                new GeoPoint(-7.406160896442139,112.67279430346964)
        )),(float)826.4));


        edgeList.add(new Edge("11-17",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.406160896442139, 112.67279430346964),
                new GeoPoint(-7.406168918758826, 112.67331918842075),
                new GeoPoint(-7.406037581236828, 112.67377487048311),
                new GeoPoint(-7.405513593841317, 112.6751053091908),
                new GeoPoint(-7.40544443809792, 112.67571148842816),
                new GeoPoint(-7.405335384788211, 112.67579253701392),
                new GeoPoint(-7.405337911647102, 112.67649835004762),
                new GeoPoint(-7.405569317417189, 112.67734592809633),
                new GeoPoint(-7.405808702568753, 112.67797892942384),
                new GeoPoint(-7.406395812058449, 112.6791820641241),
                new GeoPoint(-7.40653727073611, 112.67950320507273),
                new GeoPoint(-7.406667602381865, 112.68023276592479),
                new GeoPoint(-7.406723458789686, 112.68074506784663),
                new GeoPoint(-7.406780752734752, 112.68138923215317),
                new GeoPoint(-7.406849908268487, 112.68182911443161),
                new GeoPoint(-7.406847248440478, 112.6825459857183),
                new GeoPoint(-7.406919063791371, 112.68300196125084),
                new GeoPoint(-7.407079579878411, 112.68334431582876)
        )),(float)1218.5));

        edgeList.add(new Edge("17-11",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.407079579878411,112.68334431582876),
                new GeoPoint(-7.406919063791371,112.68300196125084),
                new GeoPoint(-7.406847248440478,112.6825459857183),
                new GeoPoint(-7.406849908268487,112.68182911443161),
                new GeoPoint(-7.406780752734752,112.68138923215317),
                new GeoPoint(-7.406723458789686,112.68074506784663),
                new GeoPoint(-7.406667602381865,112.68023276592479),
                new GeoPoint(-7.40653727073611,112.67950320507273),
                new GeoPoint(-7.406395812058449,112.6791820641241),
                new GeoPoint(-7.405808702568753,112.67797892942384),
                new GeoPoint(-7.405569317417189,112.67734592809633),
                new GeoPoint(-7.405337911647102,112.67649835004762),
                new GeoPoint(-7.405335384788211,112.67579253701392),
                new GeoPoint(-7.40544443809792,112.67571148842816),
                new GeoPoint(-7.405513593841317,112.6751053091908),
                new GeoPoint(-7.406037581236828,112.67377487048311),
                new GeoPoint(-7.406168918758826,112.67331918842075),
                new GeoPoint(-7.406160896442139,112.67279430346964)
        )),(float)1218.5));


        edgeList.add(new Edge("10-9",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.394110307670756, 112.67386067768741),
                new GeoPoint(-7.389814066303561, 112.67572184343872)
        )),(float)520.5));

        edgeList.add(new Edge("9-10",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.389814066303561,112.67572184343872),
                new GeoPoint(-7.394110307670756,112.67386067768741)
        )),(float)520.5));


        edgeList.add(new Edge("9-7",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.389814066303561, 112.67572184343872),
                new GeoPoint(-7.386054420659774, 112.67740246866543)
        )),(float)457.8));

        edgeList.add(new Edge("7-9",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.386054420659774,112.67740246866543),
                new GeoPoint(-7.389814066303561,112.67572184343872)
        )),(float)457.8));


        edgeList.add(new Edge("8-7",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.392601664885367, 112.67189164896546),
                new GeoPoint(-7.392085481345143, 112.67214179549092),
                new GeoPoint(-7.389914983719052, 112.6729679158675),
                new GeoPoint(-7.389648990629333, 112.6729142716872),
                new GeoPoint(-7.388499898641404, 112.67320395026081),
                new GeoPoint(-7.3869320837505485, 112.6738315871703),
                new GeoPoint(-7.387011882184955, 112.67459333453053),
                new GeoPoint(-7.386990602603848, 112.6746952584731),
                new GeoPoint(-7.386910804165612, 112.6747328093993),
                new GeoPoint(-7.386481473430731, 112.67471860119872),
                new GeoPoint(-7.386417634607758, 112.6748044318872),
                new GeoPoint(-7.386316556452506, 112.67577539155059),
                new GeoPoint(-7.386486793332239, 112.67715404698424),
                new GeoPoint(-7.386054420659774, 112.67740246866543)
        )),(float)1153.9));

        edgeList.add(new Edge("7-8",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.386054420659774,112.67740246866543),
                new GeoPoint(-7.386486793332239,112.67715404698424),
                new GeoPoint(-7.386316556452506,112.67577539155059),
                new GeoPoint(-7.386417634607758,112.6748044318872),
                new GeoPoint(-7.386481473430731,112.67471860119872),
                new GeoPoint(-7.386910804165612,112.6747328093993),
                new GeoPoint(-7.386990602603848,112.6746952584731),
                new GeoPoint(-7.387011882184955,112.67459333453053),
                new GeoPoint(-7.3869320837505485,112.6738315871703),
                new GeoPoint(-7.388499898641404,112.67320395026081),
                new GeoPoint(-7.389648990629333,112.6729142716872),
                new GeoPoint(-7.389914983719052,112.6729679158675),
                new GeoPoint(-7.392085481345143,112.67214179549092),
                new GeoPoint(-7.392601664885367,112.67189164896546)
        )),(float)1153.9));


        edgeList.add(new Edge("8-9",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.392601664885367, 112.67189164896546),
                new GeoPoint(-7.3920258271721515, 112.6722025521003),
                new GeoPoint(-7.389940447019304, 112.67293211295235),
                new GeoPoint(-7.388963890518919, 112.67309572770226),
                new GeoPoint(-7.389006449490032, 112.67342295720208),
                new GeoPoint(-7.3889346312238935, 112.67358388974297),
                new GeoPoint(-7.388793654593467, 112.67366972043145),
                new GeoPoint(-7.388790994656606, 112.67378237321007),
                new GeoPoint(-7.388913351735406, 112.67427589966881),
                new GeoPoint(-7.389200624743726, 112.67440464570153),
                new GeoPoint(-7.389708671921554, 112.67546411826241),
                new GeoPoint(-7.389814066303561, 112.67572184343872)
        )),(float)767.1));

        edgeList.add(new Edge("9-8",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.389814066303561,112.67572184343872),
                new GeoPoint(-7.389708671921554,112.67546411826241),
                new GeoPoint(-7.389200624743726,112.67440464570153),
                new GeoPoint(-7.388913351735406,112.67427589966881),
                new GeoPoint(-7.388790994656606,112.67378237321007),
                new GeoPoint(-7.388793654593467,112.67366972043145),
                new GeoPoint(-7.3889346312238935,112.67358388974297),
                new GeoPoint(-7.389006449490032,112.67342295720208),
                new GeoPoint(-7.388963890518919,112.67309572770226),
                new GeoPoint(-7.389940447019304,112.67293211295235),
                new GeoPoint(-7.3920258271721515,112.6722025521003),
                new GeoPoint(-7.392601664885367,112.67189164896546)
        )),(float)767.1));


        edgeList.add(new Edge("10-19",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.394110307670756, 112.67386067768741),
                new GeoPoint(-7.394346502678151, 112.67448672011155),
                new GeoPoint(-7.394942320638929, 112.6757205362584),
                new GeoPoint(-7.394995518632052, 112.6772869463231),
                new GeoPoint(-7.394793366224123, 112.67856367781418),
                new GeoPoint(-7.394772087017902, 112.67902501776474),
                new GeoPoint(-7.394963599836943, 112.67966874792832),
                new GeoPoint(-7.395761569021987, 112.68083819105881),
                new GeoPoint(-7.396135407272974, 112.6814034171171),
                new GeoPoint(-7.396188605122297, 112.68236364794443),
                new GeoPoint(-7.396204564475851, 112.68427874518106),
                new GeoPoint(-7.395951578784166, 112.68554057391935),
                new GeoPoint(-7.396095213022448, 112.68600191386992),
                new GeoPoint(-7.3965523715148445, 112.68683407952739)
        )),(float)1516.1));

        edgeList.add(new Edge("19-10",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3965523715148445,112.68683407952739),
                new GeoPoint(-7.396095213022448,112.68600191386992),
                new GeoPoint(-7.395951578784166,112.68554057391935),
                new GeoPoint(-7.396204564475851,112.68427874518106),
                new GeoPoint(-7.396188605122297,112.68236364794443),
                new GeoPoint(-7.396135407272974,112.6814034171171),
                new GeoPoint(-7.395761569021987,112.68083819105881),
                new GeoPoint(-7.394963599836943,112.67966874792832),
                new GeoPoint(-7.394772087017902,112.67902501776474),
                new GeoPoint(-7.394793366224123,112.67856367781418),
                new GeoPoint(-7.394995518632052,112.6772869463231),
                new GeoPoint(-7.394942320638929,112.6757205362584),
                new GeoPoint(-7.394346502678151,112.67448672011155),
                new GeoPoint(-7.394110307670756,112.67386067768741)
        )),(float)1516.1));


        edgeList.add(new Edge("19-20",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3965523715148445, 112.68683407952739),
                new GeoPoint(-7.39673358684929, 112.68812622340971),
                new GeoPoint(-7.396680389065652, 112.68892015727812),
                new GeoPoint(-7.396611231937339, 112.6894941500073),
                new GeoPoint(-7.396001619623017, 112.69027517414068),
                new GeoPoint(-7.395807447359719, 112.69073919630026),
                new GeoPoint(-7.395836706199378, 112.69098327732061),
                new GeoPoint(-7.396145253845014, 112.69158945655798),
                new GeoPoint(-7.396754368822592, 112.69255607947052),
                new GeoPoint(-7.396727769932833, 112.69295036419571),
                new GeoPoint(-7.3966556781597035, 112.69334502028632),
                new GeoPoint(-7.396749249057729, 112.6939729227097)
        )),(float)847.8));

        edgeList.add(new Edge("20-19",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.396749249057729,112.6939729227097),
                new GeoPoint(-7.3966556781597035,112.69334502028632),
                new GeoPoint(-7.396727769932833,112.69295036419571),
                new GeoPoint(-7.396754368822592,112.69255607947052),
                new GeoPoint(-7.396145253845014,112.69158945655798),
                new GeoPoint(-7.395836706199378,112.69098327732061),
                new GeoPoint(-7.395807447359719,112.69073919630026),
                new GeoPoint(-7.396001619623017,112.69027517414068),
                new GeoPoint(-7.396611231937339,112.6894941500073),
                new GeoPoint(-7.396680389065652,112.68892015727812),
                new GeoPoint(-7.39673358684929,112.68812622340971),
                new GeoPoint(-7.3965523715148445,112.68683407952739)
        )),(float)847.8));


        edgeList.add(new Edge("20-21",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.396749249057729, 112.6939729227097),
                new GeoPoint(-7.3966556781597035, 112.69410944985556),
                new GeoPoint(-7.395841751224933, 112.69429184006857),
                new GeoPoint(-7.394368167269956, 112.69450641678976),
                new GeoPoint(-7.3936978711358226, 112.69454396771597),
                new GeoPoint(-7.393373362324351, 112.69470490025687),
                new GeoPoint(-7.393238181710282, 112.69489560261083)
        )),(float)417.7));

        edgeList.add(new Edge("21-20",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.393238181710282,112.69489560261083),
                new GeoPoint(-7.393373362324351,112.69470490025687),
                new GeoPoint(-7.3936978711358226,112.69454396771597),
                new GeoPoint(-7.394368167269956,112.69450641678976),
                new GeoPoint(-7.395841751224933,112.69429184006857),
                new GeoPoint(-7.3966556781597035,112.69410944985556),
                new GeoPoint(-7.396749249057729,112.6939729227097)
        )),(float)417.7));


        edgeList.add(new Edge("19-17",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3965523715148445, 112.68683407952739),
                new GeoPoint(-7.39679180727851, 112.68687504128796),
                new GeoPoint(-7.397249307892741, 112.68687504128796),
                new GeoPoint(-7.397504656866563, 112.68690722779614),
                new GeoPoint(-7.3979345928807065, 112.68706621404755),
                new GeoPoint(-7.3985836035584605, 112.68710912939179),
                new GeoPoint(-7.401647772224475, 112.68710912939179),
                new GeoPoint(-7.402030791811107, 112.68711985822785),
                new GeoPoint(-7.402573402322743, 112.68710912939179),
                new GeoPoint(-7.402796829986441, 112.68703402753937),
                new GeoPoint(-7.4031692091743535, 112.68468172726396),
                new GeoPoint(-7.40682382822853, 112.68359275040392),
                new GeoPoint(-7.407079579878411, 112.68334431582876)
        )),(float)1427.0));

        edgeList.add(new Edge("17-19",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.407079579878411,112.68334431582876),
                new GeoPoint(-7.40682382822853,112.68359275040392),
                new GeoPoint(-7.4031692091743535,112.68468172726396),
                new GeoPoint(-7.402796829986441,112.68703402753937),
                new GeoPoint(-7.402573402322743,112.68710912939179),
                new GeoPoint(-7.402030791811107,112.68711985822785),
                new GeoPoint(-7.401647772224475,112.68710912939179),
                new GeoPoint(-7.3985836035584605,112.68710912939179),
                new GeoPoint(-7.3979345928807065,112.68706621404755),
                new GeoPoint(-7.397504656866563,112.68690722779614),
                new GeoPoint(-7.397249307892741,112.68687504128796),
                new GeoPoint(-7.39679180727851,112.68687504128796),
                new GeoPoint(-7.3965523715148445,112.68683407952739)
        )),(float)1427.0));


        edgeList.add(new Edge("15-14",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.413242534499598, 112.67411856483085),
                new GeoPoint(-7.41413090326859, 112.67413810943222),
                new GeoPoint(-7.41563101891739, 112.67403618548965),
                new GeoPoint(-7.41631105813039, 112.67403381739386),
                new GeoPoint(-7.417726053963288, 112.67388897810706),
                new GeoPoint(-7.418125018131579, 112.67379778300055),
                new GeoPoint(-7.419161149483131, 112.67321842585334),
                new GeoPoint(-7.419772892398148, 112.67305749331244),
                new GeoPoint(-7.420389953955046, 112.67296629820594),
                new GeoPoint(-7.420720149714339, 112.67296925376449)
        )),(float)854.));

        edgeList.add(new Edge("14-15",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.420720149714339,112.67296925376449),
                new GeoPoint(-7.420389953955046,112.67296629820594),
                new GeoPoint(-7.419772892398148,112.67305749331244),
                new GeoPoint(-7.419161149483131,112.67321842585334),
                new GeoPoint(-7.418125018131579,112.67379778300055),
                new GeoPoint(-7.417726053963288,112.67388897810706),
                new GeoPoint(-7.41631105813039,112.67403381739386),
                new GeoPoint(-7.41563101891739,112.67403618548965),
                new GeoPoint(-7.41413090326859,112.67413810943222),
                new GeoPoint(-7.413242534499598,112.67411856483085)
        )),(float)854.));


        edgeList.add(new Edge("13-14",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.4193370805415, 112.6623262483934),
                new GeoPoint(-7.419334875535774, 112.66272594470321),
                new GeoPoint(-7.419318917019876, 112.66383101481735),
                new GeoPoint(-7.419350834051112, 112.6659124090129),
                new GeoPoint(-7.419733838245374, 112.66665269870101),
                new GeoPoint(-7.419813630743885, 112.66710867423355),
                new GeoPoint(-7.420010452178337, 112.66797997053149),
                new GeoPoint(-7.420510484615776, 112.67005600030902),
                new GeoPoint(-7.420570165060367, 112.67157554626465),
                new GeoPoint(-7.420436011735588, 112.6719979196358),
                new GeoPoint(-7.420720149714339, 112.67296925376449)
        )),(float)1202.2));

        edgeList.add(new Edge("14-13",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.420720149714339,112.67296925376449),
                new GeoPoint(-7.420436011735588,112.6719979196358),
                new GeoPoint(-7.420570165060367,112.67157554626465),
                new GeoPoint(-7.420510484615776,112.67005600030902),
                new GeoPoint(-7.420010452178337,112.66797997053149),
                new GeoPoint(-7.419813630743885,112.66710867423355),
                new GeoPoint(-7.419733838245374,112.66665269870101),
                new GeoPoint(-7.419350834051112,112.6659124090129),
                new GeoPoint(-7.419318917019876,112.66383101481735),
                new GeoPoint(-7.419334875535774,112.66272594470321),
                new GeoPoint(-7.4193370805415,112.6623262483934)
        )),(float)1202.2));


        edgeList.add(new Edge("12-13",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.4046151537143015, 112.66644538151999),
                new GeoPoint(-7.405700367709185, 112.66590873853957),
                new GeoPoint(-7.406232334377956, 112.6657531704167),
                new GeoPoint(-7.406328088310156, 112.66570489065444),
                new GeoPoint(-7.406284102708187, 112.66522745744979),
                new GeoPoint(-7.407151206900277, 112.66502360956466),
                new GeoPoint(-7.40818321825507, 112.66487877027785),
                new GeoPoint(-7.412106904008565, 112.66402046339309),
                new GeoPoint(-7.413825129050505, 112.663644954131),
                new GeoPoint(-7.414683383546641, 112.66338200361237),
                new GeoPoint(-7.41582176821993, 112.66316206247315),
                new GeoPoint(-7.417777233607311, 112.66268891272024),
                new GeoPoint(-7.418506007796868, 112.66250259111563),
                new GeoPoint(-7.4193370805415, 112.6623262483934)
        )),(float)1749.7));

        edgeList.add(new Edge("13-12",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.4193370805415,112.6623262483934),
                new GeoPoint(-7.418506007796868,112.66250259111563),
                new GeoPoint(-7.417777233607311,112.66268891272024),
                new GeoPoint(-7.41582176821993,112.66316206247315),
                new GeoPoint(-7.414683383546641,112.66338200361237),
                new GeoPoint(-7.413825129050505,112.663644954131),
                new GeoPoint(-7.412106904008565,112.66402046339309),
                new GeoPoint(-7.40818321825507,112.66487877027785),
                new GeoPoint(-7.407151206900277,112.66502360956466),
                new GeoPoint(-7.406284102708187,112.66522745744979),
                new GeoPoint(-7.406328088310156,112.66570489065444),
                new GeoPoint(-7.406232334377956,112.6657531704167),
                new GeoPoint(-7.405700367709185,112.66590873853957),
                new GeoPoint(-7.4046151537143015,112.66644538151999)
        )),(float)1749.7));


        edgeList.add(new Edge("14-16",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.420720149714339, 112.67296925376449),
                new GeoPoint(-7.421296127844998, 112.67445255597211),
                new GeoPoint(-7.4213333642138535, 112.6747207768736),
                new GeoPoint(-7.4216223615535215, 112.67545926445437),
                new GeoPoint(-7.421771306899599, 112.6754109846921),
                new GeoPoint(-7.422170267399539, 112.6765451461594),
                new GeoPoint(-7.4223511293736335, 112.67752147024082),
                new GeoPoint(-7.422585185935585, 112.67846560781406),
                new GeoPoint(-7.422622422195309, 112.67947948282169),
                new GeoPoint(-7.422769760159163, 112.68053045066537),
                new GeoPoint(-7.422721884983531, 112.68129756244363),
                new GeoPoint(-7.422583578891339, 112.68166770728769),
                new GeoPoint(-7.4226899681968925, 112.68339504989328),
                new GeoPoint(-7.422583578891339, 112.6834701517457),
                new GeoPoint(-7.422512892964231, 112.68353217790617)
        )),(float)1234.3));

        edgeList.add(new Edge("16-14",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.422512892964231,112.68353217790617),
                new GeoPoint(-7.422583578891339,112.6834701517457),
                new GeoPoint(-7.4226899681968925,112.68339504989328),
                new GeoPoint(-7.422583578891339,112.68166770728769),
                new GeoPoint(-7.422721884983531,112.68129756244363),
                new GeoPoint(-7.422769760159163,112.68053045066537),
                new GeoPoint(-7.422622422195309,112.67947948282169),
                new GeoPoint(-7.422585185935585,112.67846560781406),
                new GeoPoint(-7.4223511293736335,112.67752147024082),
                new GeoPoint(-7.422170267399539,112.6765451461594),
                new GeoPoint(-7.421771306899599,112.6754109846921),
                new GeoPoint(-7.4216223615535215,112.67545926445437),
                new GeoPoint(-7.4213333642138535,112.6747207768736),
                new GeoPoint(-7.421296127844998,112.67445255597211),
                new GeoPoint(-7.420720149714339,112.67296925376449)
        )),(float)1234.3));


        edgeList.add(new Edge("15-16",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.413242534499598, 112.67411856483085),
                new GeoPoint(-7.413625544009011, 112.67555227073373),
                new GeoPoint(-7.4144128402876746, 112.67859926017465),
                new GeoPoint(-7.414668179318968, 112.67995109351816),
                new GeoPoint(-7.415285248033207, 112.68128146918954),
                new GeoPoint(-7.4164129920691835, 112.68274059089364),
                new GeoPoint(-7.416717298648352, 112.68435528072061),
                new GeoPoint(-7.416977955876706, 112.68457522185983),
                new GeoPoint(-7.419472810108939, 112.68407020768902),
                new GeoPoint(-7.420462236654608, 112.68391626483375),
                new GeoPoint(-7.4212548401853295, 112.68373923903877),
                new GeoPoint(-7.421701676445616, 112.68368023044044),
                new GeoPoint(-7.422512892964231, 112.68353217790617)
        )),(float)1876.));

        edgeList.add(new Edge("16-15",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.422512892964231,112.68353217790617),
                new GeoPoint(-7.421701676445616,112.68368023044044),
                new GeoPoint(-7.4212548401853295,112.68373923903877),
                new GeoPoint(-7.420462236654608,112.68391626483375),
                new GeoPoint(-7.419472810108939,112.68407020768902),
                new GeoPoint(-7.416977955876706,112.68457522185983),
                new GeoPoint(-7.416717298648352,112.68435528072061),
                new GeoPoint(-7.4164129920691835,112.68274059089364),
                new GeoPoint(-7.415285248033207,112.68128146918954),
                new GeoPoint(-7.414668179318968,112.67995109351816),
                new GeoPoint(-7.4144128402876746,112.67859926017465),
                new GeoPoint(-7.413625544009011,112.67555227073373),
                new GeoPoint(-7.413242534499598,112.67411856483085)
        )),(float)1876.));


        edgeList.add(new Edge("6-7",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.3903954431817285, 112.6678323469003),
                new GeoPoint(-7.388579053468651, 112.66870515547683),
                new GeoPoint(-7.386887329573431, 112.66902702055862),
                new GeoPoint(-7.386695813252701, 112.66946690283706),
                new GeoPoint(-7.386578775460278, 112.67027156554153),
                new GeoPoint(-7.386185102658321, 112.67111914359023),
                new GeoPoint(-7.386238301706103, 112.67179506026199),
                new GeoPoint(-7.386195742468383, 112.67265336714675),
                new GeoPoint(-7.386280860939713, 112.67337219916274),
                new GeoPoint(-7.386025505476555, 112.67445581160476),
                new GeoPoint(-7.385653111828327, 112.6764621039479),
                new GeoPoint(-7.386054420659774, 112.67740246866543)
        )),(float)1372.7));

        edgeList.add(new Edge("7-6",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.386054420659774,112.67740246866543),
                new GeoPoint(-7.385653111828327,112.6764621039479),
                new GeoPoint(-7.386025505476555,112.67445581160476),
                new GeoPoint(-7.386280860939713,112.67337219916274),
                new GeoPoint(-7.386195742468383,112.67265336714675),
                new GeoPoint(-7.386238301706103,112.67179506026199),
                new GeoPoint(-7.386185102658321,112.67111914359023),
                new GeoPoint(-7.386578775460278,112.67027156554153),
                new GeoPoint(-7.386695813252701,112.66946690283706),
                new GeoPoint(-7.386887329573431,112.66902702055862),
                new GeoPoint(-7.388579053468651,112.66870515547683),
                new GeoPoint(-7.3903954431817285,112.6678323469003)
        )),(float)1372.7));


        edgeList.add(new Edge("7-22",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.386054420659774, 112.67740246866543),
                new GeoPoint(-7.386619508287842, 112.67888145647933),
                new GeoPoint(-7.386683347081667, 112.67909603320052),
                new GeoPoint(-7.386406712241823, 112.67995970450332),
                new GeoPoint(-7.3862949942766605, 112.68075900278976),
                new GeoPoint(-7.38643331175319, 112.68156366549422),
                new GeoPoint(-7.3870238204929795, 112.6830710669606),
                new GeoPoint(-7.387811164252006, 112.68500225745132),
                new GeoPoint(-7.387273855487384, 112.68525438509872),
                new GeoPoint(-7.387247256026615, 112.68542068205764),
                new GeoPoint(-7.387373026448214, 112.6860779766235)
        )),(float)1047.0));

        edgeList.add(new Edge("22-7",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.387373026448214,112.6860779766235),
                new GeoPoint(-7.387247256026615,112.68542068205764),
                new GeoPoint(-7.387273855487384,112.68525438509872),
                new GeoPoint(-7.387811164252006,112.68500225745132),
                new GeoPoint(-7.3870238204929795,112.6830710669606),
                new GeoPoint(-7.38643331175319,112.68156366549422),
                new GeoPoint(-7.3862949942766605,112.68075900278976),
                new GeoPoint(-7.386406712241823,112.67995970450332),
                new GeoPoint(-7.386683347081667,112.67909603320052),
                new GeoPoint(-7.386619508287842,112.67888145647933),
                new GeoPoint(-7.386054420659774,112.67740246866543)
        )),(float)1047.0));


        edgeList.add(new Edge("22-21",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.387373026448214, 112.6860779766235),
                new GeoPoint(-7.387464729666312, 112.6864470250362),
                new GeoPoint(-7.387757323502674, 112.68749845097004),
                new GeoPoint(-7.3880711966753925, 112.6880670792812),
                new GeoPoint(-7.388581419247321, 112.6877613074535),
                new GeoPoint(-7.388964450171358, 112.68838357994495),
                new GeoPoint(-7.389496437014972, 112.68915605614124),
                new GeoPoint(-7.389890106866824, 112.6897783286327),
                new GeoPoint(-7.390001823923048, 112.6902825839275),
                new GeoPoint(-7.390134820381709, 112.69045960972248),
                new GeoPoint(-7.390352934487222, 112.69060444900929),
                new GeoPoint(-7.3906505288554305, 112.69124449620557),
                new GeoPoint(-7.391359777158781, 112.6915312184988),
                new GeoPoint(-7.392260307757128, 112.69282136103496),
                new GeoPoint(-7.393052966734932, 112.69474249681862),
                new GeoPoint(-7.393185962274805, 112.69497048458489),
                new GeoPoint(-7.393238181710282, 112.69489560261083)
        )),(float)1283.5));

        edgeList.add(new Edge("21-22",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.393238181710282,112.69489560261083),
                new GeoPoint(-7.393185962274805,112.69497048458489),
                new GeoPoint(-7.393052966734932,112.69474249681862),
                new GeoPoint(-7.392260307757128,112.69282136103496),
                new GeoPoint(-7.391359777158781,112.6915312184988),
                new GeoPoint(-7.3906505288554305,112.69124449620557),
                new GeoPoint(-7.390352934487222,112.69060444900929),
                new GeoPoint(-7.390134820381709,112.69045960972248),
                new GeoPoint(-7.390001823923048,112.6902825839275),
                new GeoPoint(-7.389890106866824,112.6897783286327),
                new GeoPoint(-7.389496437014972,112.68915605614124),
                new GeoPoint(-7.388964450171358,112.68838357994495),
                new GeoPoint(-7.388581419247321,112.6877613074535),
                new GeoPoint(-7.3880711966753925,112.6880670792812),
                new GeoPoint(-7.387757323502674,112.68749845097004),
                new GeoPoint(-7.387464729666312,112.6864470250362),
                new GeoPoint(-7.387373026448214,112.6860779766235)
        )),(float)1283.5));


        edgeList.add(new Edge("7-23",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.386054420659774, 112.67740246866543),
                new GeoPoint(-7.383955976771587, 112.6778982242051),
                new GeoPoint(-7.380912965239943, 112.67811280092629),
                new GeoPoint(-7.378146572952725, 112.67837029299172),
                new GeoPoint(-7.372549895672889, 112.67875653108986),
                new GeoPoint(-7.370634666667861, 112.67864924272926),
                new GeoPoint(-7.367314916775793, 112.67897110781105),
                new GeoPoint(-7.366570097678599, 112.67912131151589),
                new GeoPoint(-7.363420563088327, 112.68019419512184),
                new GeoPoint(-7.360143320911192, 112.68090229830177),
                new GeoPoint(-7.359153640825903, 112.68099572644356)
        )),(float)3036.4));

        edgeList.add(new Edge("23-7",new ArrayList<GeoPoint>(Arrays.asList(
                new GeoPoint(-7.359153640825903,112.68099572644356),
                new GeoPoint(-7.360143320911192,112.68090229830177),
                new GeoPoint(-7.363420563088327,112.68019419512184),
                new GeoPoint(-7.366570097678599,112.67912131151589),
                new GeoPoint(-7.367314916775793,112.67897110781105),
                new GeoPoint(-7.370634666667861,112.67864924272926),
                new GeoPoint(-7.372549895672889,112.67875653108986),
                new GeoPoint(-7.378146572952725,112.67837029299172),
                new GeoPoint(-7.380912965239943,112.67811280092629),
                new GeoPoint(-7.383955976771587,112.6778982242051),
                new GeoPoint(-7.386054420659774,112.67740246866543)
        )),(float)3036.4));

        for (final Edge edge:edgeList){
            firebaseFirestore.collection("Edges")
                    .document(edge.getNode()).set(edge)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GraphActivity.this, "edge gagal "+edge.getNode(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
