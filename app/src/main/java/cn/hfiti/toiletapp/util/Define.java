package cn.hfiti.toiletapp.util;

import cn.hfiti.toiletapp.db.DBManager;

public class Define {
	
	public static String DB_NAMES="toiletinfo.db";
	public static final int  DB_VERSION=1;
	public static final String USER_TABLE_NAME="userinfo";
	public static final String WEIGHT_TABLE_NAME="weightinfo";
	
	public static final String USER_TABLE_DROP="DROP TABLE IF EXISTS " + USER_TABLE_NAME;
	public static final String WEIGHT_TABLE_DROP="DROP TABLE IF EXISTS " + WEIGHT_TABLE_NAME;
	public static final String USER_TABLE_CREATE="CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME +
			" (_id integer primary key autoincrement,"
			+ "user_id_name,"
			+ "user_password,"
			+ "user_name,"
			+ "user_sex,"
			+ "user_birthday,"
			+ "user_high,"
			+ "user_weight)";
	public static final String WEIGHT_TABLE_CREATE="CREATE TABLE IF NOT EXISTS " + WEIGHT_TABLE_NAME +
			" (_id integer primary key autoincrement,"
			+"user_id_name,"
			+ "user_name,"
			+ "record_time,"
			+ "user_weight,"
			+ "g_time)";
	
	public static String USER_ID_NAME;
	public static String USER_NAME;
	public static DBManager dbManager;
	public static boolean LOGIN_SUCCESS = false;
	
	//注册信息保存
	public static int WEIGHT = 0;
	public static long CREAT_TIME = 0;
	
	//参数保存
	public static int SET_SEAT_TEMP = 2;
	public static int SET_WATER_TEMP = 2;
	public static int SET_WATER_STH = 2;
	public static int SET_DRY_TEMP = 2;
	
	//功能开关状态保存
	public static boolean DRY_STATE = false;
	public static boolean FLUSH_STATE = false;
	public static boolean COVER_STATE = false;
	public static boolean SMELL_STATE = false;
	public static boolean POWER_STATE = false;
	
	//马桶控制指令集
	public static String TEST="5500aa";
	public static String HIP_CLEAN="550101aa";
	public static String LADY_CLEAN="550102aa";
	public static String NOZZLE_CLEAN="550141aa";
	public static String CHILDREN_CLEAN="550103aa";
	public static String HIP_BATH="550104aa";
	public static String HIP_MASSAGE="550105aa";
	public static String DRY="550106aa";
	public static String N_FRONT="550107aa";
	public static String N_BACK="550108aa";
	public static String FLUSH="550109aa";
	public static String COVER="550110aa";
	public static String LIGHT_ON="550111aa";
	public static String LIGHT_OFF="550112aa";
	
	public static String SET_SEAT_TEMP_1="550113aa";
	public static String SET_SEAT_TEMP_2="550114aa";
	public static String SET_SEAT_TEMP_3="550115aa";
	public static String SET_SEAT_TEMP_4="550116aa";
	public static String SET_WATER_TEMP_1="550117aa";
	public static String SET_WATER_TEMP_2="550118aa";
	public static String SET_WATER_TEMP_3="550119aa";
	public static String SET_WATER_TEMP_4="550120aa";
	public static String SET_DRY_TEMP_1="550121aa";
	public static String SET_DRY_TEMP_2="550122aa";
	public static String SET_DRY_TEMP_3="550123aa";
	public static String SET_DRY_TEMP_4="550124aa";
	public static String SET_WATER_STH_1="550125aa";
	public static String SET_WATER_STH_2="550126aa";
	public static String SET_WATER_STH_3="550127aa";
	public static String SET_WATER_STH_4="550128aa";
	
	public static String AUTO_DRY_OPEN="550129aa";
	public static String AUTO_DRY_CLOSE="550130aa";
	public static String AUTO_FLUSH_OPEN="550131aa";
	public static String AUTO_FLUSH_CLOSE="550132aa";
	public static String AUTO_COVER_OPEN="550133aa";
	public static String AUTO_COVER_CLOSE="550134aa";
	public static String AUTO_POWER_OPEN="550135aa";
	public static String AUTO_POWER_CLOSE="550136aa";
	public static String AUTO_SMELL_OPEN="550137aa";
	public static String AUTO_SMELL_CLOSE="550138aa";	
	public static String POWER_CONTROL="550139aa";
	public static String END_INQ="550140aa";
	
	public static String SEARCH_SEAT_TEMP="550203aa";
	public static String SEARCH_WEIGHT="550201aa";
	public static String SEARCH_HEART_RATE="550204aa";

    public static final String PH = "    肾脏参与机体内酸碱平衡调节，这种调节能力可以通过尿液pH 反映出来。" +
            "由于内源性酸产生偏多，尿液pH 普遍偏酸，约为5.0～6.0。一般情况下，饭后会出现“碱潮”现象，尿液" +
            "偏碱；酸性尿多见于进食肉食过多和某些种类的水果（如酸果蔓果实）、代谢性酸中毒、呼吸性酸中毒以" +
            "及使用排结石药物（如碳酸钙）；碱性尿多见于进食素食和柑橘类水果、代谢性碱中毒、呼吸性碱中毒、" +
            "一些肾脏疾病（如肾小管性酸中毒）等。\n" +
            "    pH 检测需新鲜尿标本，如果尿液放置时间过久，大多数细菌可分解尿素而释放氨，尿呈碱性；但有时也会" +
            "出现相反的情况；尿中碳酸缓冲对释放CO2，逸出至空气，尿pH 值增高。\n";
    public static final String NIT = "    正常饮食中含有硝酸盐，并以硝酸盐形式而非亚硝酸盐形式从尿液排泄。" +
            "尿路感染时，致病菌大多数含有硝酸还原酶，可以将硝酸盐还原为亚硝酸盐。影响亚硝酸盐的形成的因素" +
            "有：病原菌必须能够利用硝酸盐；尿液在膀胱贮留4 h 或以上；饮食中含有充分的硝酸盐。\n" +
            "尿中亚硝酸盐测定常用于尿路感染的快速筛选试验。测定结果与尿液细菌培养结果的吻合率为60%。假阳" +
            "性可能源于体内一氧化氮氧化成亚硝酸盐从尿液排出。一些药物代谢产物或标本收集不当，会导致假阳性；" +
            "大量维生素C 和影响亚硝酸盐形成因素，可导致假阴性。\n";
    public static final String GLU = "    尿中出现葡萄糖，主要由于肾前因素－高血糖导致肾小球滤过的葡萄糖" +
            "超出肾小管的重吸收阈值或肾性因素－肾小管重吸收能力下降。如果尿糖阳性，应结合临床区别是生" +
            "理性糖尿还是病理性糖尿。生理性糖尿多见于饮食过度、应急状态和妊娠；病理性糖尿多见于血糖升" +
            "高引起的糖尿、肾小管功能受损所导致的肾性糖尿以及一些内分泌异常（如甲状腺功能亢进、嗜铬细" +
            "胞瘤等）所引发的糖尿。服用大剂量维生素C 或一些新型抗生素可以使结果呈假阳性，而高浓度酮体" +
            "尿和高比重尿可以出现假阴性。\n";
    public static final String VC = "    正常人尿液中维生素C 浓度过低，常见于维生素C 缺乏病（坏血酸病）；" +
            "尿维生素C长期增高，可能与肾结石形成有关。尿中维生素C浓度主要反映最近饮食中维生素C 摄入情况。" +
            "尿中维生素C 对试纸条法测定的其他项目有影响（如尿糖、尿红细胞、尿胆红素、尿亚硝酸盐）。\n";
    public static final String SG = "    尿比重是指在4℃条件下尿液与同体积纯水的重量之比，取决于尿中溶解物质" +
            "的浓度，与固体总量成正比。尿比重测定，以SG表示，其结果的特点是每0.005为一个梯度，最低为1.000，最" +
            "高为1.030。用比重计或折射仪测定的尿比重可以有各种测量值。尿比重的测定对了解肾脏的浓缩和稀释功能" +
            "很有用处。\n" +
            "    正常人尿比重可因饮食和饮水、出汗和排尿等情况的不同而有较大的波动，但一般应在1.010到1.025之" +
            "间，可以因各种情况波动在1.003到1.030之间。比如大量饮水，可使尿比重低至1.003，而少饮水多出汗之" +
            "后，尿比重可升高到1.030以上。此外婴儿的尿比重多低于成人，病理情况下还可因尿中含有较多的蛋白质、" +
            "葡萄糖、酮体和各种细胞而增加。\n";
    public static final String BLD = "    试纸条法尿隐血试验阳性，应高度怀疑：①血尿。多见于肾脏和泌尿系统的" +
            "一些疾病（如肾小球肾炎、肾盂肾炎、肾囊肿、泌尿系统结石和肿瘤等）、肾外疾病、外伤、剧烈运动和一些药" +
            "物（如环磷酰胺）。②血红蛋白尿。常见于血管内溶血（如输血反应和溶血性贫血）、严重烧伤、剧烈运动（行军" +
            "性血红蛋白尿）和一些感染，另外，尿中红细胞破坏后也可释放血红蛋白。③肌红蛋白尿。常见于肌肉损伤（如严" +
            "重挤压伤、外科手术、缺血）、肌肉消耗性疾病、皮肌炎、过度运动等。尿隐血试验阳性，应进一步显微镜镜检确" +
            "认有无红细胞。尿中含有对热不稳定酶或菌尿时，检测结果呈假阳性；尿中存在大量维生素C 时，检测结果亦呈假阴性。\n";
    public static final String PRO = "    正常生理情况下，少量蛋白从肾小球滤过，几乎在近端小管完全重吸收。因此，" +
            "蛋白尿出现往往提示肾小球滤过屏障受损和（或）肾小管重吸收能力降低。肾小球性蛋白尿常伴大分子量蛋白质丢" +
            "失，一般> 1.5g/24 h；肾小管性蛋白尿常为少量小分子量蛋白，一般< 2.0 g/24 h。剧烈体育运动、脱水或发" +
            "热、或妊娠时，尿中可出现少量蛋白质。临床上蛋白尿可由多种原因引发，应结合临床具体情况分析。\n" +
            "    试纸条法测定尿蛋白是半定量筛选蛋白尿的方法。试纸条法对尿中不同蛋白质的敏感性不同，对白蛋白" +
            "最为敏感。多发性骨髓瘤患者尿中可以出现大量游离轻链，但试纸条法检测阴性。如果容器内有去垢剂残留，" +
            "试纸条法尿蛋白检测可以假阳性，造影剂也会导致出现假阳性；尿液偏碱时，会破坏指示剂所在的缓冲范围而出" +
            "现假阴性；尿液pH < 3.0 时，试纸条法也会出现假阴性。阳性者需进一步进行尿蛋白定量测定。\n";
    public static final String BIL = "    如果血胆红素水平升高，尿试纸条法可以检测到胆红素阳性。某些肝脏疾病如" +
            "病毒性肝炎，可以出现尿胆红素升高。如果明确有血胆红素升高而尿胆红素阴性或可疑阳性，建议做特异性实验验" +
            "证。尿胆红素阳性，也可以是肝内胆管堵塞引起，常见于总胆管结石、胰头癌、肝内炎症时管内压力增加所致胆汁" +
            "反流。尿中一些药物（如嘧啶）的代谢产物在低pH 时有颜色，与检测物质本身反应的颜色相近，可以出现假阳性；" +
            "胆红素见光易分解，尿液不新鲜或见光时间过长，检测结果可出现假阴性；尿中大量维生素C 或亚硝酸盐会降低试纸" +
            "条法的敏感性。\n";
    public static final String URO = "    直接胆红素分泌入小肠腔后，经过一系列反应生成系列产物，尿胆原为主要产物" +
            "之一，约20%的尿胆原被重吸收，进入肝肠循环，其中少量（2% ～ 5%）进入血流从肾小球滤过。检测结果结合尿胆" +
            "红素结果分析，有助于黄疸的鉴别诊断。尿中一些药物代谢产物可与试纸块内试剂反应，导致假阳性；尿液中卟胆原、" +
            "吲哚类化合物以及黑色素原等，也常导致假阳性；使用甲醛作为防腐剂或标本存放不当，尿胆原被氧化成尿胆素，检测" +
            "结果可呈假阴性。\n";
    public static final String KET = "当机体不能有效利用葡萄糖、脂肪酸代谢不完全，可导致大量酮体产生，此时尿液就会" +
            "出现酮体。除了糖尿病酮症酸中毒外，酮尿也可见于长期饥饿、急性发热、低糖类饮食、中毒引起的呕吐、腹泻等情况" +
            "。降压药物甲基多巴、卡托普利以及一些双胍类降糖药（如苯乙双胍，商品名降糖灵），可使尿酮体检测呈阳性。苯丙酮" +
            "尿症、尿液中存在酞类染料、防腐剂（8-羟喹啉）、左旋多巴的代谢产物等会导致检测结果呈假阳性；试纸条活性降低和" +
            "酮体降解，会使检测结果呈假阴性。\n";
    public static final String WBC = "    正常人尿中可有少量白细胞，但尿白细胞酯酶阴性。如果酯酶试验阳性，高度提示有尿路感" +
            "染。某些肾脏病如狼疮性肾炎、急性间质性肾炎、肾移植排斥反应，尿液中白细胞也可升高。尿路滴虫和尿液中一些氧化性" +
            "物质、药物代谢产物，可导致假阳性；嗜酸细胞和组织细胞也会使结果呈假阳性。尿液中蛋白质、葡萄糖或维生素C 过高，" +
            "可导致假阴性。如果尿白细胞酯酶阳性，必须进一步行显微镜镜检，以确认有无白细胞存在。\n";


}
